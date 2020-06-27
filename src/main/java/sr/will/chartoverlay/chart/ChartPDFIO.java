package sr.will.chartoverlay.chart;

import org.apache.commons.io.FileUtils;
import org.ghost4j.document.DocumentException;
import org.ghost4j.document.PDFDocument;
import org.ghost4j.renderer.RendererException;
import org.ghost4j.renderer.SimpleRenderer;
import sr.will.chartoverlay.ChartOverlay;
import sr.will.chartoverlay.chart.kap.RasterChart;
import sr.will.chartoverlay.util.FileUtil;

import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ChartPDFIO {

    public static File downloadPDF(String chart) {
        try {
            File pdf = new File(FileUtils.getTempDirectoryPath(), chart + ".pdf");
            ChartOverlay.LOGGER.info("Downloading chart {} pdf...", chart);
            FileUtils.copyURLToFile(new URL("https://www.charts.noaa.gov/PDFs/{chart}.pdf".replace("{chart}", chart)), pdf);
            ChartOverlay.LOGGER.info("Done downloading chart {} pdf", chart);
            return pdf;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writePNG(String chart, File pdf, RasterChart chartInfo) {
        PDFDocument document = new PDFDocument();
        SimpleRenderer renderer = new SimpleRenderer();
        renderer.setResolution(chartInfo.kapHeaders.get(0).paneInfo.dpi);
        try {
            ChartOverlay.LOGGER.info("Opening PDF...");
            long startTime = System.currentTimeMillis();
            document.load(pdf);
            List<Image> images = renderer.render(document);
            long endTime = System.currentTimeMillis();
            ChartOverlay.LOGGER.info("Done reading {}, took {}ms", chart, endTime - startTime);

            if (images.size() > 1) {
                ChartOverlay.LOGGER.warn("PDF has more than 1 image! ({})", images.size());
            }

            startTime = System.currentTimeMillis();
            FileUtil.writeImage((RenderedImage) images.get(0), ChartIO.CHART_DIR, chart);
            endTime = System.currentTimeMillis();
            ChartOverlay.LOGGER.info("Done writing {}, took {}ms", chart, endTime - startTime);
        } catch (IOException | DocumentException | RendererException e) {
            e.printStackTrace();
        }
    }
}
