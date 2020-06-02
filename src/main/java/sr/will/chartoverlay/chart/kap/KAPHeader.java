package sr.will.chartoverlay.chart.kap;

import sr.will.chartoverlay.chart.Header;
import sr.will.chartoverlay.chart.generic.ChartPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KAPHeader extends Header {
    public List<KAPReferencePoint> referencePoints = new ArrayList<>();
    public Map<IndexedColorType, List<IndexedColor>> colorMap = new HashMap<>();
    public List<ChartPoint> polygonPoints = new ArrayList<>();

    public KAPHeader(String headerString) {
        super(headerString);

        /*
        // Chart corrections
        unknownItems.entrySet().removeIf(entry -> entry.getKey().matches("ARE[0-9]{4}"));

        // Publish info
        unknownItems.entrySet().removeIf(entry -> entry.getKey().matches("ADN[0-9]{4}"));

        // Chart info, contains height and width, needed for decoding image
        info = new KAPInfo(TokenOld.parseMap(unknownItems.get("BSB")));

        for (int i = 0; i < itemsLists.get("REF").size(); i++) {
            referencePoints.add(new KAPReferencePoint(
                    TokenOld.parseList(itemsLists.get("REF").get(i)),
                    TokenOld.parseList(itemsLists.get("ERR").get(i))
            ));
        }
        itemsLists.remove("REF");
        itemsLists.remove("ERR");

        // Color maps
        for (IndexedColorType colorType : IndexedColorType.values()) {
            if (!itemsLists.containsKey(colorType.getShortHand())) continue;
            colorMap.put(colorType, new ArrayList<>());
            for (String colorString : itemsLists.remove(colorType.getShortHand())) {
                colorMap.get(colorType).add(new IndexedColor(colorType, TokenOld.parseList(colorString)));
            }
        }

        // Polygon points
        for (String polygonString : itemsLists.remove("PLY")) {
            List<String> polygonParts = TokenOld.parseList(polygonString);
            polygonPoints.add(new ChartPoint(Double.parseDouble(polygonParts.get(1)), Double.parseDouble(polygonParts.get(2))));
        }
        */
    }

    public String toString() {
        return "BSBHeader [" +
                       ", \n" + super.toString() +
                       "]";
    }
}
