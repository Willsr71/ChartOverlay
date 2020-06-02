package sr.will.chartoverlay.chart.kap;

public enum ColorType {
    RGB("RGB"),
    DAY("DAY"),
    DUSK("DSK"),
    GRAY("GRY"),
    PRC("PRC"),
    PRG("PRG"),
    NGR("NGR"),
    NGT("NGT");

    private String shortHand;

    ColorType(String shortHand) {
        this.shortHand = shortHand;
    }

    public String getShortHand() {
        return shortHand;
    }
}
