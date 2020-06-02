package sr.will.chartoverlay.chart;

import org.apache.commons.io.FileUtils;
import sr.will.chartoverlay.ChartOverlay;
import sr.will.chartoverlay.chart.bsb.BSBHeader;
import sr.will.chartoverlay.chart.bsb.KAPFileInfo;
import sr.will.chartoverlay.chart.kap.KAPFile;
import sr.will.chartoverlay.chart.kap.KAPHeader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static sr.will.chartoverlay.chart.ChartManager.CHART_DIR;

public class ChartIO {
    private static char[] multiplierMask = {0, 63, 31, 15, 7, 3, 1, 0};

    public static RasterChart read(String chart) throws IOException {
        String chartDir = CHART_DIR + chart + "/";
        BSBHeader header = new BSBHeader(FileUtils.readFileToString(new File(chartDir + chart + ".BSB"), StandardCharsets.UTF_8));
        RasterChart rasterChart = new RasterChart(header);

        for (KAPFileInfo kapFileInfo : header.kapFileInfos) {
            rasterChart.addKAPFile(readKAP(chartDir + kapFileInfo.fileName));
        }

        return rasterChart;
    }

    public static KAPFile readKAP(String file) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        KAPHeader header = getKAPHeader(inputStream);
        getKAPImage(inputStream, header.info.width, header.info.height);

        inputStream.close();
        return new KAPFile(header);
    }

    public static void write(OutputStream outputStream) {

    }

    public static KAPHeader getKAPHeader(InputStream inputStream) throws IOException {
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

        return new KAPHeader(header.toString());
    }

    public static void getKAPImage(InputStream inputStream, int width, int height) throws IOException {
        int imageDepth = inputStream.read();
        List<Byte> bytes = new ArrayList<>();
        height = 10;

        ChartOverlay.LOGGER.info("Image ({}, {}) depth: {}", width, height, imageDepth);
        for (int i = 0; i < height; i++) {
            readRow(inputStream, i, width, imageDepth);
        }
    }

    public static void readRow(InputStream inputStream, int row, int width, int depth) throws IOException {
        int c;
        int pixel;
        int multiplier;
        int rowNum = 0;

        do {
            c = inputStream.read();
            rowNum = ((rowNum & 0x7f) << 7) + c;
        } while (c >= 0x80);

        ChartOverlay.LOGGER.info("Row: {}, calculated row: {}", row, rowNum);

        while ((c = inputStream.read()) != 0) {
            pixel = (c & 0x7f) >> (7 - depth);
            multiplier = c & multiplierMask[depth];

            ChartOverlay.LOGGER.info("pixel: {}, multiplier: {}", pixel, multiplier);

            while (c >= 0x80) {
                c = inputStream.read();
                multiplier = (multiplier << 7) + (c & 0x7f);
                ChartOverlay.LOGGER.info("multiplier: {}", multiplier);
            }
            multiplier++;

            if (multiplier > width) {
                multiplier = width;
                ChartOverlay.LOGGER.warn("Multiplier greater than width: {} > {}", multiplier, width);
            }
        }
    }
}
