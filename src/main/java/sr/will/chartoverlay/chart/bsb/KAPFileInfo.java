package sr.will.chartoverlay.chart.bsb;

import sr.will.chartoverlay.chart.Header;

import java.util.Map;

public class KAPFileInfo {
    public int id;
    public String name;
    public String number;
    public String type;
    public String fileName;

    public KAPFileInfo(String item, String info) {
        id = Integer.parseInt(item.substring(1));
        Map<String, String> items = Header.parseMap(info);
        name = items.get("NA");
        number = items.get("NU");
        type = items.get("TY");
        fileName = items.get("FN");
    }

    public String toString() {
        return "KAPFile [id=" + id +
                       ", name=" + name +
                       ", number=" + number +
                       ", type=" + type +
                       ", fileName=" + fileName +
                       "]";
    }
}
