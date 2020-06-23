package sr.will.chartoverlay.chart;

import org.apache.commons.io.FileUtils;
import sr.will.chartoverlay.ChartOverlay;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static sr.will.chartoverlay.chart.ChartIO.CHART_DIR;

public class ChartBSBIO {

    public static void downloadBSB(String chart) {
        try {
            File zipFile = new File(FileUtils.getTempDirectoryPath() + "/" + chart + ".zip");
            ChartOverlay.LOGGER.info("Downloading chart {}...", chart);
            FileUtils.copyURLToFile(new URL("https://www.charts.noaa.gov/RNCs/{chart}.zip".replace("{chart}", chart)), zipFile);
            ChartOverlay.LOGGER.info("Extracting chart {}...", chart);
            removeBSBFiles(chart);
            extractFolder(FileUtils.openInputStream(zipFile));
            ChartOverlay.LOGGER.info("Done extracting chart {}", chart);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeBSBFiles(String chart) throws IOException {
        File folder = new File(CHART_DIR, chart);
        FileUtils.deleteDirectory(folder);
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
