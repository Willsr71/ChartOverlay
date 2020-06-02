package sr.will.chartoverlay.chart.kap;

import sr.will.chartoverlay.chart.Header;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KAPHeader extends Header {
    public KAPInfo info;
    public List<KAPReferencePoint> referencePoints = new ArrayList<>();
    public Map<ColorType, List<ColorIndex>> colorMap = new HashMap<>();

    public KAPHeader(String headerString) {
        super(headerString);

        // Chart corrections
        items.entrySet().removeIf(entry -> entry.getKey().matches("ARE[0-9]{4}"));

        // Publish info
        items.entrySet().removeIf(entry -> entry.getKey().matches("ADN[0-9]{4}"));

        // Chart info, contains height and width, needed for decoding image
        info = new KAPInfo(parseMap(items.remove("BSB")));

        for (int i = 0; i < itemsLists.get("REF").size(); i++) {
            referencePoints.add(new KAPReferencePoint(
                    parseList(itemsLists.get("REF").get(i)),
                    parseList(itemsLists.get("ERR").get(i))
            ));
        }
        itemsLists.remove("REF");
        itemsLists.remove("ERR");

        for (ColorType colorType : ColorType.values()) {
            if (!itemsLists.containsKey(colorType.getShortHand())) continue;
            colorMap.put(colorType, new ArrayList<>());
            for (String colorString : itemsLists.remove(colorType.getShortHand())) {
                colorMap.get(colorType).add(new ColorIndex(colorType, parseList(colorString)));
            }
        }
    }

    public String toString() {
        return "BSBHeader [" +
                       ", \n" + super.toString() +
                       "]";
    }
}
