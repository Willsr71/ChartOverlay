package sr.will.chartoverlay.chart;

import org.apache.commons.io.FileUtils;
import sr.will.chartoverlay.ChartOverlay;
import sr.will.chartoverlay.chart.kap.RasterChart;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ChartManager {
    public static final String CHART_DIR = "catalog/charts/";

    private static Map<String, List<Thread>> activeDownloads = new HashMap<>();

    public static RasterChart fetchBSB(String chart) {
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
            downloadBSB(chart);
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

    public static void downloadBSB(String chart) {
        try {
            File zipFile = new File(FileUtils.getTempDirectoryPath() + "/" + chart + ".zip");
            ChartOverlay.LOGGER.info("Downloading chart {}...", chart);
            FileUtils.copyURLToFile(new URL("https://www.charts.noaa.gov/RNCs/{chart}.zip".replace("{chart}", chart)), zipFile);
            ChartOverlay.LOGGER.info("Extracting chart {}...", chart);
            extractFolder(FileUtils.openInputStream(zipFile));
            ChartOverlay.LOGGER.info("Done extracting chart {}", chart);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void extractFolder(InputStream inputStream) throws IOException {
        File destDir = new File(CHART_DIR);

        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(inputStream);
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            if (zipEntry.getName().endsWith("USERAGREEMENT.TXT")) {
                zipEntry = zis.getNextEntry();
                continue;
            }
            File newFile = newFile(destDir, zipEntry);
            newFile.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(newFile);
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
    }

    private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        String name = zipEntry.getName();
        if (name.startsWith("BSB_ROOT/")) name = name.substring(9);
        File destFile = new File(destinationDir, name);

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }
}
