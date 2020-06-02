package sr.will.chartoverlay.chart.token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ListToken extends Token {
    public ListToken(String id, String name, Token subToken) {
        super(id, name, Collections.singletonList(subToken));
    }

    public ListToken(String id, String name) {
        super(id, name);
    }

    public List<Object> parse(String string) {
        if (subTokens.size() == 0) return new ArrayList<>(Arrays.asList(string.split(",")));
        ArrayList<Object> list = new ArrayList<>();
        for (String object : string.split(",")) {
            list.add(subTokens.get(0).parse(object));
        }

        return list;
    }
}
