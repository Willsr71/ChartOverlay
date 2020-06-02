package sr.will.chartoverlay.chart.kap;

import sr.will.chartoverlay.chart.generic.ChartPoint;

import java.util.List;

public class KAPReferencePoint extends ChartPoint {
    public int id;
    public double error1;
    public double error2;
    public double error3;
    public double error4;

    public KAPReferencePoint(List<String> items, List<String> error) {
        super(
                Integer.parseInt(items.get(1)),
                Integer.parseInt(items.get(2)),
                Double.parseDouble(items.get(3)),
                Double.parseDouble(items.get(4))
        );
        id = Integer.parseInt(items.get(0));
        error1 = Double.parseDouble(error.get(1));
        error2 = Double.parseDouble(error.get(2));
        error3 = Double.parseDouble(error.get(3));
        error4 = Double.parseDouble(error.get(4));
    }

    public String toString() {
        return "KAPFile [id=" + id +
                       ", x=" + x +
                       ", y=" + y +
                       ", lat=" + latitude +
                       ", long=" + longitude +
                       "]";
    }
}
