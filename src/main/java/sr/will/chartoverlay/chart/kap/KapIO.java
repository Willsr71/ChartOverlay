package sr.will.chartoverlay.chart.kap;

import org.apache.commons.io.FileUtils;
import sr.will.chartoverlay.ChartOverlay;
import sr.will.chartoverlay.chart.json.BSBHeader;
import sr.will.chartoverlay.chart.json.KAPHeader;
import sr.will.chartoverlay.util.FileUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static sr.will.chartoverlay.chart.ChartManager.CHART_DIR;

public class KapIO {
    private static final char[] multiplierMask = {0, 63, 31, 15, 7, 3, 1, 0};

    public static RasterChart processRaster(String chart) throws IOException {
        // Double json conversion is temporary until a rewrite of the header parser
        RasterChart rasterChart = new RasterChart(ChartOverlay.GSON.fromJson(ChartOverlay.GSON.toJson(
                new Header(FileUtils.readFileToString(new File(CHART_DIR + chart, chart + ".BSB"), StandardCharsets.UTF_8))
        ), BSBHeader.class));

        for (BSBHeader.KapFile kapFile : rasterChart.header.kapFiles) {
            FileInputStream inputStream = new FileInputStream(new File(CHART_DIR + chart, kapFile.fileName));
            // Double json conversion is temporary
            KAPHeader kapHeader = ChartOverlay.GSON.fromJson(ChartOverlay.GSON.toJson(getKAPHeader(inputStream)), KAPHeader.class);
            rasterChart.addKAPHeader(kapHeader);
            ImageIO.write(getKAPImage(inputStream, kapHeader), "png", new File(CHART_DIR + chart, kapFile.number + ".png"));

            inputStream.close();
        }

        FileUtil.writeGson(rasterChart, CHART_DIR + chart, chart, "Header " + chart);
        return rasterChart;
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

    public static BufferedImage getKAPImage(InputStream inputStream, KAPHeader header) throws IOException {
        int imageDepth = inputStream.read();
        BufferedImage bufferedImage = new BufferedImage(header.paneInfo.dimensions.width, header.paneInfo.dimensions.height, BufferedImage.TYPE_INT_RGB);

        ChartOverlay.LOGGER.info("Image ({}, {}) depth: {}", header.paneInfo.dimensions.width, header.paneInfo.dimensions.height, imageDepth);
        for (int i = 0; i < header.paneInfo.dimensions.height; i++) {
            readRow(inputStream, bufferedImage, header, i, header.paneInfo.dimensions.width, imageDepth);
        }

        return bufferedImage;
    }

    public static void readRow(InputStream inputStream, BufferedImage bufferedImage, KAPHeader header, int row, int width, int depth) throws IOException {
        int c;
        int pixel = 0;
        int multiplier;
        int rowNum = 0;
        int written = 0;

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
                    if (written < width)
                        bufferedImage.setRGB(written++, row, header.colorRGB.get(pixel).getColor().getRGB());
                } catch (ArrayIndexOutOfBoundsException e) {
                    ChartOverlay.LOGGER.error("({}, {}), pixel: {}, width: {}", row, written, pixel, width);
                    throw e;
                }
            }
        }

        if (written < width) {
            int shortFall = width - written;
            if (shortFall < 8) {
                while (written < width)
                    bufferedImage.setRGB(written++, row, header.colorRGB.get(pixel).getColor().getRGB());
            } else {
                ChartOverlay.LOGGER.warn("Short row for row " + row + ", written=" + written + ", width=" + width);
            }
        }
    }
}
