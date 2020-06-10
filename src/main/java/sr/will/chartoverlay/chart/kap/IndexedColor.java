package sr.will.chartoverlay.chart.kap;

import java.util.List;

public class IndexedColor {
    public transient IndexedColorType type;
    public int index;
    public int red;
    public int green;
    public int blue;

    public IndexedColor(IndexedColorType type, List<String> list) {
        this.type = type;
        index = Integer.parseInt(list.get(0));
        red = Integer.parseInt(list.get(1));
        green = Integer.parseInt(list.get(2));
        blue = Integer.parseInt(list.get(3));
    }
}
