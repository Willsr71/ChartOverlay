package sr.will.chartoverlay.chart.token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapToken extends Token {
    public MapToken(String id, String name, List<Token> subTokens) {
        super(id, name, subTokens);
    }

    public MapToken(String id, String name) {
        super(id, name);
    }

    public Map<String, String> parseMap(String item) {
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

    public Map<Token, Object> parse(String item) {
        Map<String, String> stringMap = parseMap(item);
        Map<Token, Object> map = new HashMap<>();
        for (Token subToken : subTokens) {
            if (!stringMap.containsKey(subToken.getId())) continue;
            map.put(subToken, subToken.parse(stringMap.remove(subToken.getId())));
        }

        for (Map.Entry<String, String> entry : stringMap.entrySet()) {
            map.put(StringToken.unknown(entry.getKey()), entry.getValue());
        }

        return map;
    }
}
