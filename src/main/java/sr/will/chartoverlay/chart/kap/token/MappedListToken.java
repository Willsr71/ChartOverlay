package sr.will.chartoverlay.chart.kap.token;

import java.util.*;

public class MappedListToken extends Token {
    public static final List<String> COLOR_NAMES = Arrays.asList("index", "red", "green", "blue");

    private final List<String> itemNames;

    public MappedListToken(String id, String name, ListToken listToken, List<String> itemNames) {
        super(id, name, Collections.singletonList(listToken));
        this.itemNames = itemNames;
    }

    public Map<String, Object> parse(String string) {
        List<Object> list = (List<Object>) subTokens.get(0).parse(string);
        if (list.size() > itemNames.size()) {
            throw new RuntimeException("MappedListToken " + getName() + " does not have enough keys (keys: " + itemNames.size() + ", values:" + list.size() + ")");
        }

        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            map.put(itemNames.get(i), list.get(i));
        }
        return map;
    }
}
