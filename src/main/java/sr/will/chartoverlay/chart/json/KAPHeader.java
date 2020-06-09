package sr.will.chartoverlay.chart.json;

import sr.will.chartoverlay.chart.kap.IndexedColor;

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
    public List<IndexedColor> colorRGB;

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
}
