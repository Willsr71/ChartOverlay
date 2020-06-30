package sr.will.chartoverlay.chart;

import sr.will.chartoverlay.chart.kap.RasterChart;

import java.util.HashMap;
import java.util.Map;

public class ChartIO {
    public static final String CHART_DIR = "catalog/charts/";

    public static final Map<String, ChartProcessThread> processThreads = new HashMap<>();

    public static RasterChart getChart(String chart) {
        if (!processThreads.containsKey(chart)) {
            ChartProcessThread thread = new ChartProcessThread(chart, Thread.currentThread());
            processThreads.put(chart, thread);
            thread.start();
        } else {
            processThreads.get(chart).addWaitingThread(Thread.currentThread());
        }

        while (!processThreads.get(chart).done) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ignored) {
                // Ignored
            }
        }

        RasterChart rasterChart = processThreads.get(chart).rasterChart;
        processThreads.get(chart).removeWaitingThread(Thread.currentThread());
        return rasterChart;
    }
}
