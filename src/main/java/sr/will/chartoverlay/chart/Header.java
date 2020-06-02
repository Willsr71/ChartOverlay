package sr.will.chartoverlay.chart;

import sr.will.chartoverlay.chart.token.Tokens;

import java.util.*;

public abstract class Header {
    protected Map<Tokens, Object> items = new HashMap<>();
    protected Map<String, String> unknownItems = new HashMap<>();
    protected Map<String, List<String>> itemsLists = new HashMap<>();

    protected Header(String headerString) {
        String lastItem = "";
        for (String line : headerString.split("\\r?\\n")) {
            if (line.equals("!")) continue;
            if (line.startsWith("    ")) {
                if (Tokens.isMap(lastItem) || Tokens.isList(lastItem)) {
                    unknownItems.replace(lastItem, unknownItems.get(lastItem) + "," + line.substring(4));
                } else {
                    unknownItems.replace(lastItem, unknownItems.get(lastItem) + line.substring(3));
                }
                continue;
            }
            int separatorIndex = line.indexOf("/");
            lastItem = line.substring(0, separatorIndex);
            String content = line.substring(separatorIndex + 1);
            if (unknownItems.containsKey(lastItem)) {
                if (itemsLists.containsKey(lastItem)) {
                    itemsLists.get(lastItem).add(content);
                } else {
                    itemsLists.put(lastItem, new ArrayList<>(Arrays.asList(unknownItems.remove(lastItem), content)));
                }
            }

            unknownItems.put(lastItem, content);
        }

        unknownItems.keySet().removeIf(item -> itemsLists.containsKey(item));

        // Remove all tokens possible before moving on to regex matching
        for (Tokens token : Tokens.tokens()) {
            if (unknownItems.containsKey(token.id())) {
                items.put(token, token.getToken().parse(unknownItems.remove(token.id())));
            }
        }

        // Regex matching
        for (Tokens token : Tokens.regexTokens()) {
            Iterator<Map.Entry<String, String>> iterator = unknownItems.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                if (!entry.getKey().matches(token.id())) continue;
                if (!items.containsKey(token)) items.put(token, new ArrayList<>());
                ((ArrayList) items.get(token)).add(token.getToken().parse(unknownItems.get(entry.getKey())));
                iterator.remove();
            }
        }
    }
}
