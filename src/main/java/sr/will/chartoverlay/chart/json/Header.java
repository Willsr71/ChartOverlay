package sr.will.chartoverlay.chart.json;

import java.io.Serializable;

public class Header implements Serializable {
    public String version;
    public String copyright;
    public ChartEdition chartEdition;
    public NoticeToMariners noticeToMariners;

    public static class ChartEdition implements Serializable {
        public int sourceEdition;
        public int rasterEdition;
        public String editionDate;
    }

    public static class NoticeToMariners implements Serializable {
        public String editionNumber;
        public String date;
    }
}
