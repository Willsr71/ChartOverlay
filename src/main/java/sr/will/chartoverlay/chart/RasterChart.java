package sr.will.chartoverlay.chart;

import sr.will.chartoverlay.chart.bsb.BSBHeader;
import sr.will.chartoverlay.chart.kap.KAPFile;

import java.util.ArrayList;
import java.util.List;

public class RasterChart {
    public BSBHeader header;
    public List<KAPFile> kapFiles = new ArrayList<>();

    public RasterChart(BSBHeader bsbHeader) {
        this.header = bsbHeader;
    }

    void addKAPFile(KAPFile kapFile) {
        kapFiles.add(kapFile);
    }

    public String toString() {
        return "RasterChart [header=" + header +
                       ", kapFiles=" + kapFiles +
                       "]";
    }
}