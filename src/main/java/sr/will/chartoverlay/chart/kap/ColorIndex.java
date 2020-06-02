package sr.will.chartoverlay.chart.kap;

import java.awt.*;
import java.util.List;

public class ColorIndex {
    public transient ColorType type;
    public transient int index;
    public transient Color color;
    public int red;
    public int green;
    public int blue;

    public ColorIndex(ColorType type, List<String> list) {
        this.type = type;
        index = Integer.parseInt(list.get(0));
        red = Integer.parseInt(list.get(1));
        green = Integer.parseInt(list.get(2));
        blue = Integer.parseInt(list.get(3));
        color = new Color(red, green, blue);
    }
}