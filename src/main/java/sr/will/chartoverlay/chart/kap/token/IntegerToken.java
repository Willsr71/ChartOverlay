package sr.will.chartoverlay.chart.kap.token;

public class IntegerToken extends Token {
    public static final IntegerToken BLANK = new IntegerToken(null, null);

    public IntegerToken(String id, String name) {
        super(id, name);
    }

    public Integer parse(String input) {
        return Integer.parseInt(input);
    }
}
