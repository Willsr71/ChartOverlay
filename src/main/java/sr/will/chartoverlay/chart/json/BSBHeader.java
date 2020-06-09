package sr.will.chartoverlay.chart.json;

import java.io.Serializable;
import java.util.List;

public class BSBHeader extends Header implements Serializable {
    public ChartInfo chartInfo;
    public String chartFormat;
    public List<Integer> coastGuardDistrict;
    public List<Integer> region;
    public List<Integer> kapInfo;
    public String organisation;
    public String manufacturer;
    public List<KapFile> kapFiles;

    public static class ChartInfo implements Serializable {
        public String name;
        public String number;
    }

    public static class KapFile implements Serializable {
        public String name;
        public int number;
        public String type;
        public String fileName;
    }
}
