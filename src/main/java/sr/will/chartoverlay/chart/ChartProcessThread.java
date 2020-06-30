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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static sr.will.chartoverlay.chart.ChartIO.CHART_DIR;

public class ChartProcessThread extends Thread {
    private final String chart;
    private final List<Thread> waitingThreads = new ArrayList<>();

    public boolean done = false;
    public RasterChart rasterChart = null;

    public ChartProcessThread(String chart, Thread initiatingThread) {
        this.chart = chart;
        waitingThreads.add(initiatingThread);
    }

    public void run() {
        ChartOverlay.LOGGER.info("New thread {} processing chart {}", Thread.currentThread(), chart);

        rasterChart = getRasterChart();
        done = true;

        waitingThreads.forEach(Thread::interrupt);
    }

    public synchronized void addWaitingThread(Thread thread) {
        waitingThreads.add(thread);
    }

    public synchronized void removeWaitingThread(Thread thread) {
        waitingThreads.remove(thread);

        if (waitingThreads.size() == 0) {
            ChartIO.processThreads.remove(chart);
            ChartOverlay.LOGGER.info("Removing thread {} for chart {}", Thread.currentThread(), chart);
        }
    }

    private RasterChart getRasterChart() {
        if (!ChartOverlay.catalog.chartsByNumber.containsKey(chart)) return null;

        if (bsbExists(chart)) return read(chart);

        ChartBSBIO.downloadBSB(chart);
        File tempFile = ChartPDFIO.downloadPDF(chart);

        RasterChart rasterChart = read(chart);
        ChartPDFIO.writePNG(chart, tempFile, rasterChart);
        return rasterChart;
    }

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

    public static boolean bsbExists(String chart) {
        String[] list = new File(CHART_DIR).list();
        if (list == null) return false;
        return Arrays.asList(list).contains(chart);
    }

    public static RasterChart read(String chart) {
        try {
            if (!isCurrent(chart)) KapIO.processRaster(chart);
            return FileUtil.readJson(new File(CHART_DIR + chart, chart + ".json"), RasterChart.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
