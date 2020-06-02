package sr.will.chartoverlay.chart;

import sr.will.chartoverlay.ChartOverlay;

import java.util.*;

public abstract class Header {
    private static final List<String> lists = Arrays.asList(
            "ARE[0-9]{4}",
            "CHK",
            "RGB",
            "PWX",
            "PWY",
            "WPX",
            "WPY"
    );
    private static final List<String> maps = Arrays.asList(
            "K[0-9]{2}",
            "CHT",
            "CED",
            "NTM",
            "BSB",
            "KNP",
            "KNQ"
    );

    protected Map<String, String> items = new HashMap<>();
    protected Map<String, List<String>> itemsLists = new HashMap<>();

    protected Header(String headerString) {
        String lastItem = "";
        for (String line : headerString.split("\\r?\\n")) {
            if (line.equals("!")) continue;
            if (line.startsWith("    ")) {
                if (isMap(lastItem) || isList(lastItem)) {
                    items.replace(lastItem, items.get(lastItem) + "," + line.substring(4));
                } else {
                    items.replace(lastItem, items.get(lastItem) + line.substring(3));
                }
                continue;
            }
            int separatorIndex = line.indexOf("/");
            lastItem = line.substring(0, separatorIndex);
            String content = line.substring(separatorIndex + 1);
            if (items.containsKey(lastItem)) {
                if (itemsLists.containsKey(lastItem)) {
                    itemsLists.get(lastItem).add(content);
                } else {
                    itemsLists.put(lastItem, new ArrayList<>(Arrays.asList(items.remove(lastItem), content)));
                }
            }

            items.put(lastItem, content);
        }

        items.keySet().removeIf(item -> itemsLists.containsKey(item));
    }

    public boolean isList(String item) {
        for (String list : lists) {
            if (item.matches(list)) return true;
        }
        return false;
    }

    public boolean isMap(String item) {
        for (String map : maps) {
            if (item.matches(map)) return true;
        }
        return false;
    }

    public static List<String> parseList(String string) {
        return new ArrayList<>(Arrays.asList(string.split(",")));
    }

    public static Map<String, String> parseMap(String item) {
        Map<String, String> map = new HashMap<>();
        String lastItem = "";
        for (String section : item.split(",")) {
            if (!section.contains("=")) {
                map.put(lastItem, map.get(lastItem) + "," + section);
                continue;
            }
            String[] parts = section.split("=");
            map.put(parts[0], parts[1]);
            lastItem = parts[0];
        }
        return map;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(", items=[");
        for (String item : items.keySet()) {
            sb.append(item).append("=");
            if (isMap(item)) {
                sb.append("[");
                Map<String, String> itemValue = parseMap(items.get(item));
                for (String part : itemValue.keySet()) {
                    sb.append(part).append("=").append(itemValue.get(part)).append(", ");
                }
                sb.delete(sb.length() - 2, sb.length()).append("]");
            } else if (isList(item)) {
                sb.append(parseList(items.get(item)));
            } else {
                sb.append(items.get(item));
            }
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length()).append("]");
        return sb.toString();
    }
}
