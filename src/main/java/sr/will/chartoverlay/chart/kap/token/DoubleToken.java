package sr.will.chartoverlay.chart.kap.token;

public class DoubleToken extends Token {
    public static final DoubleToken BLANK = new DoubleToken(null, null);

    public DoubleToken(String id, String name) {
        super(id, name);
    }

    public Double parse(String input) {
        return Double.parseDouble(input);
    }
}
