package sr.will.chartoverlay.util;

import org.json.JSONObject;
import org.json.XML;
import sr.will.chartoverlay.ChartOverlay;
import sr.will.chartoverlay.descriptor.catalog.json.Catalog;

import java.io.*;
import java.lang.reflect.Type;

public class FileUtil {
    private static File getFolder(String name) {
        File folder = new File(name);
        if (folder.mkdirs()) ChartOverlay.LOGGER.info("Created folder(s)");
        return folder;
    }

    public static void processProductCatalog(String name) {
        ChartOverlay.LOGGER.info("Processing {}", name);
        try {
            JSONObject json = XML.toJSONObject(new InputStreamReader(new FileInputStream(new File(getFolder("catalog"), name + ".xml"))));
            json = (JSONObject) json.get("DS_Series");
            writeJSON(name + "_raw", json);

            Catalog catalog = new Catalog(json);
            ChartOverlay.LOGGER.info("Processed {} charts!", catalog.charts.size());

            writeGson(catalog, "catalog", name, "Catalog");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
