package sr.will.chartoverlay.chart;

import sr.will.chartoverlay.ChartOverlay;
import sr.will.chartoverlay.chart.kap.KapIO;
import sr.will.chartoverlay.chart.kap.RasterChart;
import sr.will.chartoverlay.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ChartIO {
    public static final String CHART_DIR = "catalog/charts/";

    private static final Map<String, List<Thread>> activeDownloads = new HashMap<>();
    private static final Map<String, RasterChart> activeCaches = new HashMap<>();

    public static boolean isCurrent(String chart) {
        File infoFile = new File(CHART_DIR + chart, chart + ".json");
        if (!Files.exists(infoFile.toPath())) {
            return false;
        }

        RasterChart chartInfo = FileUtil.readJson(infoFile, RasterChart.class);

        LocalDate catalogDate = LocalDate.parse(ChartOverlay.catalog.chartsByNumber.get(chart).revisionDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate chartDate = LocalDate.parse(chartInfo.header.noticeToMariners.date, DateTimeFormatter.ofPattern("MM/dd/yyyy"));

        return !catalogDate.isAfter(chartDate);
    }

    public static RasterChart getChart(String chart) {
        if (!ChartOverlay.catalog.chartsByNumber.containsKey(chart)) return null;

        while (activeDownloads.containsKey(chart)) {
            if (!activeDownloads.get(chart).contains(Thread.currentThread())) {
                activeDownloads.get(chart).add(Thread.currentThread());
            }

            try {
                ChartOverlay.LOGGER.info("Active download for chart {}, waiting", chart);
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                ChartOverlay.LOGGER.info("Download wait thread interrupted");
            }
        }

        if (!bsbExists(chart)) {
            activeDownloads.put(chart, new ArrayList<>());
            ChartBSBIO.downloadBSB(chart);
            File tempFile = ChartPDFIO.downloadPDF(chart);
            // Interrupt all waiting download threads
            activeDownloads.remove(chart).forEach(Thread::interrupt);
        }



        try {
            return ChartIO.read(chart);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static boolean bsbExists(String chart) {
        String[] list = new File(CHART_DIR).list();
        if (list == null) return false;
        return Arrays.asList(list).contains(chart);
    }

    private static boolean imageExists(String chart) {
        return Files.exists(new File(CHART_DIR + chart, chart + ".png").toPath());
    }

    public static void update(String chart) throws IOException {
        if (isCurrent(chart)) return;
        KapIO.processRaster(chart);
    }

    public static RasterChart read(String chart) throws IOException {
        update(chart);
        return FileUtil.readJson(new File(CHART_DIR + chart, chart + ".json"), RasterChart.class);
    }
}
