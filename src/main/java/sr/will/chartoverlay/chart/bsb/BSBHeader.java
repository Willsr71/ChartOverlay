package sr.will.chartoverlay.chart.bsb;

import sr.will.chartoverlay.chart.Header;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BSBHeader extends Header {
    public List<KAPFileInfo> kapFileInfos = new ArrayList<>();

    public BSBHeader(String headerString) {
        super(headerString);

        Iterator<Map.Entry<String, String>> iterator = items.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            if (!entry.getKey().matches("K[0-9]{2}")) continue;
            kapFileInfos.add(new KAPFileInfo(entry.getKey(), entry.getValue()));
            iterator.remove();
        }
    }

    public String toString() {
        return "BSBHeader [kapFiles=" + kapFileInfos.toString() +
                       ", \n" + super.toString() +
                       "]";
    }
}
