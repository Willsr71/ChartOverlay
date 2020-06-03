package sr.will.chartoverlay.chart.token;

public class StringToken extends Token {
    public static final StringToken BLANK = new StringToken(null, null);

    public StringToken(String id, String name) {
        super(id, name);
    }

    public String parse(String input) {
        return input;
    }

    public static StringToken unknown(String id) {
        return new StringToken(id, id);
    }
}
