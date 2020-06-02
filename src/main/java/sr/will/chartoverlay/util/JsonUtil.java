package sr.will.chartoverlay.util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JsonUtil {

    public static List<String> getPath(String pathString) {
        if (pathString.contains(".")) return Arrays.asList(pathString.split("\\."));
        return Collections.singletonList(pathString);
    }

    private static JSONObject get(JSONObject jsonObject, List<String> path) {
        //ChartOverlay.LOGGER.info("{}", path);
        if (path.size() == 1) return jsonObject;
        return get(jsonObject.getJSONObject(path.get(0)), path.subList(1, path.size()));
    }

    public static JSONObject getJsonObject(JSONObject jsonObject, List<String> path) {
        return get(jsonObject, path).getJSONObject(path.get(path.size() - 1));
    }

    public static JSONObject getJsonObject(JSONObject jsonObject, String path) {
        return getJsonObject(jsonObject, getPath(path));
    }

    public static JSONArray getJsonArray(JSONObject jsonObject, List<String> path) {
        return get(jsonObject, path).getJSONArray(path.get(path.size() - 1));
    }

    public static JSONArray getJsonArray(JSONObject jsonObject, String path) {
        return getJsonArray(jsonObject, getPath(path));
    }

    public static String getString(JSONObject jsonObject, List<String> path) {
        return get(jsonObject, path).getString(path.get(path.size() - 1));
    }

    public static String getString(JSONObject jsonObject, String path) {
        return getString(jsonObject, getPath(path));
    }

    public static int getInt(JSONObject jsonObject, List<String> path) {
        return get(jsonObject, path).getInt(path.get(path.size() - 1));
    }

    public static int getInt(JSONObject jsonObject, String path) {
        return getInt(jsonObject, getPath(path));
    }

    public static Object getObject(JSONObject jsonObject, List<String> path) {
        return get(jsonObject, path).get(path.get(path.size() - 1));
    }

    public static Object getObject(JSONObject jsonObject, String path) {
        return getObject(jsonObject, getPath(path));
    }
}
