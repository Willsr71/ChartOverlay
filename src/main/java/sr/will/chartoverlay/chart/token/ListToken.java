package sr.will.chartoverlay.chart.token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ListToken extends Token {
    public static final ListToken BLANK = new ListToken(null, null);
    public static final ListToken BLANK_PIPE = new ListToken(null, null).setSplitToken("\\|");
    public static final ListToken BLANK_SEMICOLON = new ListToken(null, null).setSplitToken(";");

    private String splitToken = ",";

    public ListToken(String id, String name, List<Token> subTokens) {
        super(id, name, subTokens);
    }

    public ListToken(String id, String name, Token subToken) {
        super(id, name, Collections.singletonList(subToken));
    }

    public ListToken(String id, String name) {
        super(id, name);
    }

    public ListToken setSplitToken(String splitToken) {
        this.splitToken = splitToken;
        return this;
    }

    public List<Object> parse(String string) {
        if (subTokens.size() == 0) return new ArrayList<>(Arrays.asList(string.split(splitToken)));
        ArrayList<Object> list = new ArrayList<>();
        String[] s = string.split(splitToken);
        int lastToken = 0;
        for (int i = 0; i < s.length; i++) {
            if (subTokens.size() > i) lastToken = i;
            if (subTokens.get(lastToken) == null) continue; // Skip nulls
            list.add(subTokens.get(lastToken).parse(s[i]));
        }

        return list;
    }
}
