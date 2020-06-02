package sr.will.chartoverlay.chart.kap;

import sr.will.chartoverlay.chart.Header;

import java.util.List;
import java.util.Map;

public class KAPInfo {
    public String name;
    public int number;
    public int width;
    public int height;
    public int dpi;

    public KAPInfo(Map<String, String> info) {
        name = info.get("NA");
        number = Integer.parseInt(info.get("NU"));
        List<String> dimensions = Header.parseList(info.get("RA"));
        width = Integer.parseInt(dimensions.get(0));
        height = Integer.parseInt(dimensions.get(1));
        dpi = Integer.parseInt(info.get("DU"));
    }
}
