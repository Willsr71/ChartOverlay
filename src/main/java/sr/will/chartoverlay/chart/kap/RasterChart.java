package sr.will.chartoverlay.chart.kap;

import sr.will.chartoverlay.chart.json.BSBHeader;
import sr.will.chartoverlay.chart.json.KAPHeader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RasterChart implements Serializable {
    public BSBHeader header;
    public List<KAPHeader> kapHeaders = new ArrayList<>();

    public RasterChart(BSBHeader bsbHeader) {
        this.header = bsbHeader;
    }

    void addKAPHeader(KAPHeader kapHeader) {
        kapHeaders.add(kapHeader);
    }

    public String toString() {
        return "RasterChart [header=" + header +
                       ", kapHeaders=" + kapHeaders +
                       "]";
    }
}
