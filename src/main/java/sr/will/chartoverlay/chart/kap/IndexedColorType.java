package sr.will.chartoverlay.chart.kap;

public enum IndexedColorType {
    RGB("RGB"),
    DAY("DAY"),
    DUSK("DSK"),
    GRAY("GRY"),
    NIGHT("NGT"),
    NIGHT_RED("NGR"),
    PRC("PRC"),
    PRG("PRG");

    private String shortHand;

    IndexedColorType(String shortHand) {
        this.shortHand = shortHand;
    }

    public String getShortHand() {
        return shortHand;
    }
}
