package sr.will.chartoverlay.chart;

import sr.will.chartoverlay.chart.token.Tokens;

import java.util.*;

public class Header {
    protected Map<Tokens, Object> items = new HashMap<>();
    protected Map<String, String> unknownItems = new HashMap<>();
    protected Map<String, List<String>> unknownItemsLists = new HashMap<>();

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
                if (unknownItemsLists.containsKey(lastItem)) {
                    unknownItemsLists.get(lastItem).add(content);
                } else {
                    unknownItemsLists.put(lastItem, new ArrayList<>(Arrays.asList(unknownItems.remove(lastItem), content)));
                }
            }

            unknownItems.put(lastItem, content);
        }

        unknownItems.keySet().removeIf(item -> unknownItemsLists.containsKey(item));

        // Remove all tokens possible before moving on to regex matching
        for (Tokens token : Tokens.getByType(Tokens.TType.SINGLE_STRING)) {
            if (unknownItems.containsKey(token.id())) {
                items.put(token, token.getToken().parse(unknownItems.remove(token.id())));
            }
        }

        // Regex matching
        for (Tokens token : Tokens.getByType(Tokens.TType.SINGLE_REGEX)) {
            Iterator<Map.Entry<String, String>> iterator = unknownItems.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                if (!entry.getKey().matches(token.id())) continue;
                if (!items.containsKey(token)) items.put(token, new ArrayList<>());
                ((ArrayList) items.get(token)).add(token.getToken().parse(unknownItems.get(entry.getKey())));
                iterator.remove();
            }
        }

        for (Tokens token : Tokens.getByType(Tokens.TType.MULTI_STRING)) {
            if (unknownItemsLists.containsKey(token.id())) {
                List<Object> values = new ArrayList<>();
                for (String item : unknownItemsLists.remove(token.id())) {
                    values.add(token.getToken().parse(item));
                }
                items.put(token, values);
            }
        }
    }
}
