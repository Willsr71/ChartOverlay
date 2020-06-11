package sr.will.chartoverlay.chart.kap;

import org.apache.commons.io.FileUtils;
import sr.will.chartoverlay.ChartOverlay;
import sr.will.chartoverlay.chart.json.BSBHeader;
import sr.will.chartoverlay.chart.json.KAPHeader;
import sr.will.chartoverlay.util.FileUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static sr.will.chartoverlay.chart.ChartManager.CHART_DIR;

public class KapIO {
    private static final char[] multiplierMask = {0, 63, 31, 15, 7, 3, 1, 0};

    public static void processRaster(String chart) throws IOException {
        // Double json conversion is temporary until a rewrite of the header parser
        RasterChart rasterChart = new RasterChart(ChartOverlay.GSON.fromJson(ChartOverlay.GSON.toJson(
                new Header(FileUtils.readFileToString(new File(CHART_DIR + chart, chart + ".BSB"), StandardCharsets.UTF_8))
        ), BSBHeader.class));

        for (BSBHeader.KapFile kapFile : rasterChart.header.kapFiles) {
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(new File(CHART_DIR + chart, kapFile.fileName)));
            // Double json conversion is temporary
            KAPHeader kapHeader = ChartOverlay.GSON.fromJson(ChartOverlay.GSON.toJson(getKAPHeader(inputStream)), KAPHeader.class);
            rasterChart.addKAPHeader(kapHeader);
            writePNG(getKAPImage(inputStream, kapHeader), kapHeader, kapHeader.colors.get("RGB"), new File(CHART_DIR + chart, kapFile.number + ".png"));
            /*
            for (String color : kapHeader.colors.keySet()) {
                writePNG(image, kapHeader, kapHeader.colors.get(color), new File(CHART_DIR + chart, kapFile.number + "_" + color + ".png"));
            }*/

            inputStream.close();
        }

        FileUtil.writeGson(rasterChart, CHART_DIR + chart, chart, "info for chart " + chart);
    }

    public static void writePNG(int[] image, KAPHeader header, List<IndexedColor> colorMap, File file) throws IOException {
        ChartOverlay.LOGGER.info("Processing {} for writing", file);
        BufferedImage bufferedImage = new BufferedImage(header.paneInfo.dimensions.width, header.paneInfo.dimensions.height, BufferedImage.TYPE_BYTE_INDEXED, getIndexColorModel(colorMap));
        bufferedImage.getRaster().setPixels(0, 0, header.paneInfo.dimensions.width, header.paneInfo.dimensions.height, image);

        ChartOverlay.LOGGER.info("Writing {}", file);
        long startTime = System.currentTimeMillis();
        ImageIO.write(bufferedImage, "png", file);
        long endTime = System.currentTimeMillis();
        ChartOverlay.LOGGER.info("Done writing {}, took {}ms", file, endTime - startTime);
    }

    public static IndexColorModel getIndexColorModel(List<IndexedColor> colorMap) {
        byte[] red = new byte[colorMap.size()];
        byte[] green = new byte[colorMap.size()];
        byte[] blue = new byte[colorMap.size()];
        for (int i = 0; i < colorMap.size(); i++) {
            IndexedColor color = colorMap.get(i);
            red[i] = (byte) color.red;
            green[i] = (byte) color.green;
            blue[i] = (byte) color.blue;
        }

        return new IndexColorModel(4, colorMap.size(), red, green, blue);
    }

    public static Header getKAPHeader(InputStream inputStream) throws IOException {
        StringBuilder header = new StringBuilder();
        int c = inputStream.read();
        while (c != -1) {
            if (c == 0x1A) {
                // Read the following null byte before stopping
                c = inputStream.read();
                break;
            }
            header.append((char) c);
            c = inputStream.read();
        }

        return new Header(header.toString());
    }

    public static int[] getKAPImage(InputStream inputStream, KAPHeader header) throws IOException {
        ChartOverlay.LOGGER.info("Reading KAP {}", header.paneInfo.number);
        long startTime = System.currentTimeMillis();
        int[] image = new int[header.paneInfo.dimensions.height * header.paneInfo.dimensions.width];
        int imageDepth = inputStream.read();

        ChartOverlay.LOGGER.debug("Image ({}, {}) depth: {}", header.paneInfo.dimensions.width, header.paneInfo.dimensions.height, imageDepth);
        for (int i = 0; i < header.paneInfo.dimensions.height; i++) {
            System.arraycopy(
                    readRow(inputStream, header, i, header.paneInfo.dimensions.width, imageDepth),
                    0,
                    image,
                    header.paneInfo.dimensions.width * i,
                    header.paneInfo.dimensions.width
            );
        }

        long endTime = System.currentTimeMillis();
        ChartOverlay.LOGGER.info("Done reading KAP {}, took {}ms", header.paneInfo.number, endTime - startTime);
        return image;
    }

    public static int[] readRow(InputStream inputStream, KAPHeader header, int row, int width, int depth) throws IOException {
        int c;
        int pixel = 0;
        int multiplier;
        int rowNum = 0;
        int written = 0;
        int[] buf = new int[width];

        do {
            c = inputStream.read();
            rowNum = ((rowNum & 0x7f) << 7) + c;
        } while (c >= 0x80);

        while ((c = inputStream.read()) != '\0') {
            pixel = ((c & 0x7f) >> (7 - depth)) - 1;
            multiplier = c & multiplierMask[depth];

            while (c >= 0x80) {
                c = inputStream.read();
                multiplier = (multiplier << 7) + (c & 0x7f);
            }
            multiplier++;

            if (multiplier > width) {
                multiplier = width;
                ChartOverlay.LOGGER.warn("Multiplier greater than width: {} > {}", multiplier, width);
            }

            for (int i = 0; i < multiplier; i++) {
                try {
                    if (written < width) buf[written++] = pixel;
                } catch (ArrayIndexOutOfBoundsException e) {
                    ChartOverlay.LOGGER.error("({}, {}), pixel: {}, width: {}", row, written, pixel, width);
                    throw e;
                }
            }
        }

        if (written < width) {
            int shortFall = width - written;
            if (shortFall < 8) {
                while (written < width) buf[written++] = pixel;
            } else {
                ChartOverlay.LOGGER.warn("Short row for row " + row + ", written=" + written + ", width=" + width);
            }
        }

        return buf;
    }
}
