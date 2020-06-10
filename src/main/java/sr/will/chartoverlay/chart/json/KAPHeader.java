package sr.will.chartoverlay.chart.json;

import sr.will.chartoverlay.chart.kap.IndexedColor;
import sr.will.chartoverlay.generic.ChartPoint;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class KAPHeader extends Header implements Serializable {
    public PaneInfo paneInfo;
    public Map<String, String> paneParameters;
    public Map<String, String> additionalPaneParameters;
    public int offsetStripImageLines;
    public int colormapDepth;
    public double phaseShift;
    // Polygon translations
    public List<Double> datumShift;
    // ORG updates
    // chart updates
    public Map<String, List<IndexedColor>> colors;
    public List<ReferencePoint> referencePoints;
    public List<ChartPoint> polygon;

    public static class PaneInfo implements Serializable {
        public String name;
        public int number;
        public Dimensions dimensions;
        public int dpi;

        public static class Dimensions implements Serializable {
            public int width;
            public int height;
        }
    }

    public static class ReferencePoint extends ChartPoint implements Serializable {
        public int id;
    }
}
