package sr.will.chartoverlay.util;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.json.XML;
import sr.will.chartoverlay.ChartOverlay;
import sr.will.chartoverlay.config.Config;
import sr.will.chartoverlay.descriptor.catalog.json.Catalog;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.Files;

public class FileUtil {
    private static File getFolder(String name) {
        File folder = new File(name);
        if (folder.mkdirs()) ChartOverlay.LOGGER.info("Created folder(s)");
        return folder;
    }

    public static Config getConfig() {
        File configFile = new File("config.json");
        if (!Files.exists(configFile.toPath())) {
            Config config = new Config();
            FileUtil.writeGson(new Config(), ".", "config", "config");
            return config;
        }

        return FileUtil.readJson(new File("config.json"), Config.class);
    }

    private static String getProductCatalogName() {
        return "RNCProdCat_" + ChartOverlay.config.productCatalogNum;
    }

    public static void processProductCatalog() {
        try {
            JSONObject json = XML.toJSONObject(new InputStreamReader(new FileInputStream(new File(getFolder("catalog"), getProductCatalogName() + ".xml"))));
            json = (JSONObject) json.get("DS_Series");
            writeJSON(getProductCatalogName() + "_raw", json);

            Catalog catalog = new Catalog(json);
            ChartOverlay.LOGGER.info("Processed {} charts!", catalog.charts.size());

            writeGson(catalog, "catalog", getProductCatalogName(), "Catalog");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Catalog getProductCatalog() {
        try {
            File catalogXML = new File(getFolder("catalog"), getProductCatalogName() + ".xml");
            if (!Files.exists(catalogXML.toPath()) ||
                    System.currentTimeMillis() > ChartOverlay.config.lastCatalogFetch + ChartOverlay.config.catalogFetchInterval) {
                ChartOverlay.LOGGER.info("Product catalog out of date, fetching new one");
                FileUtils.copyURLToFile(new URL("https://www.charts.noaa.gov/RNCs/{catalog}".replace("{catalog}", getProductCatalogName() + ".xml")), catalogXML);
                processProductCatalog();
                ChartOverlay.config.lastCatalogFetch = System.currentTimeMillis();
                ChartOverlay.instance.saveConfig();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Catalog catalog = FileUtil.readJson(new File("catalog", getProductCatalogName() + ".json"), Catalog.class);
        catalog.catalogCharts();
        return catalog;
    }

    public static void writeJSON(String name, JSONObject json) {
        ChartOverlay.LOGGER.info("Writing {}.json", name);

        try {
            String jsonPrettyPrintString = json.toString(2);
            FileOutputStream fileOutputStream = new FileOutputStream(new File(getFolder("catalog"), name + ".json"));
            fileOutputStream.write(jsonPrettyPrintString.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> T readJson(File file, Type typeof) {
        try {
            FileReader fileReader = new FileReader(file);
            T obj = ChartOverlay.GSON.fromJson(fileReader, typeof);
            fileReader.close();
            return obj;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeGson(Object object, String folder, String name, String message) {
        ChartOverlay.LOGGER.info("Writing " + message + "...");
        try {
            FileWriter writer = new FileWriter(new File(getFolder(folder), name + ".json"));
            writer.write(ChartOverlay.GSON.toJson(object));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
