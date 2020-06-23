package sr.will.chartoverlay.chart;

import org.apache.commons.io.FileUtils;
import sr.will.chartoverlay.ChartOverlay;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ChartPDFIO {

    public static void downloadPDF(String chart) {
        try {
            File pdf = new File(FileUtils.getTempDirectoryPath(), chart + ".pdf");
            ChartOverlay.LOGGER.info("Downloading chart {} pdf...", chart);
            FileUtils.copyURLToFile(new URL("https://www.charts.noaa.gov/PDFs/{chart}.pdf".replace("{chart}", chart)), pdf);
            ChartOverlay.LOGGER.info("Done downloading chart {} pdf", chart);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
