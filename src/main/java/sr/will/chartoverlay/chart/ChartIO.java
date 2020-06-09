package sr.will.chartoverlay.chart;

import sr.will.chartoverlay.chart.kap.KapIO;
import sr.will.chartoverlay.chart.kap.RasterChart;
import sr.will.chartoverlay.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static sr.will.chartoverlay.chart.ChartManager.CHART_DIR;

public class ChartIO {
    public static RasterChart read(String chart) throws IOException {
        File infoFile = new File(CHART_DIR + chart, chart + ".json");
        if (!Files.exists(infoFile.toPath())) {
            return KapIO.processRaster(chart);
        }

        return FileUtil.readJson(infoFile, RasterChart.class);
    }

    public static String getChartDir(String chart) {
        return CHART_DIR + chart;
    }
}
