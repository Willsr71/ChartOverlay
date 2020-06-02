package sr.will.chartoverlay.chart.token;

import java.util.ArrayList;
import java.util.List;

public abstract class Token {
    private final String id;
    private final String name;
    protected final List<Token> subTokens;

    public Token(String id, String name, List<Token> subTokens) {
        this.id = id;
        this.name = name;
        this.subTokens = subTokens;
    }

    public Token(String id, String name) {
        this.id = id;
        this.name = name;
        subTokens = new ArrayList<>();
    }

    public abstract Object parse(String input);

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }
}
