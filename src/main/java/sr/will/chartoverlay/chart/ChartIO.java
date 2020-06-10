package sr.will.chartoverlay.chart;

import sr.will.chartoverlay.ChartOverlay;
import sr.will.chartoverlay.chart.kap.KapIO;
import sr.will.chartoverlay.chart.kap.RasterChart;
import sr.will.chartoverlay.descriptor.catalog.json.Chart;
import sr.will.chartoverlay.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static sr.will.chartoverlay.chart.ChartManager.CHART_DIR;

public class ChartIO {
    public static boolean isCurrent(String chart) {
        File infoFile = new File(CHART_DIR + chart, chart + ".json");
        if (!Files.exists(infoFile.toPath())) {
            // TODO Check if up to date
            return false;
        }

        return true;
    }

    public static void update(String chart) throws IOException {
        if (isCurrent(chart)) return;
        KapIO.processRaster(chart);
    }

    public static RasterChart read(String chart) throws IOException {
        update(chart);
        return FileUtil.readJson(new File(CHART_DIR + chart, chart + ".json"), RasterChart.class);
    }

    public static byte[] getExtentImage(int extentId, String colorIndex) throws IOException {
        return null;
    }

    public static byte[] getExtentImage(int extentId) throws IOException {
        Chart chart = ChartOverlay.catalog.getByExtent(extentId);
        if (chart == null) throw new RuntimeException("Extent does not exist");
        update(chart.chart);
        File file = new File(CHART_DIR + chart.chart, extentId + ".png");
        return Files.readAllBytes(file.toPath());
    }
}
