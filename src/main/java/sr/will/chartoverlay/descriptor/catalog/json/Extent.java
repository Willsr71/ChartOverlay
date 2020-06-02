package sr.will.chartoverlay.descriptor.catalog.json;

import org.json.JSONObject;
import sr.will.chartoverlay.ChartOverlay;
import sr.will.chartoverlay.util.JsonUtil;
import sr.will.chartoverlay.util.Parser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Extent implements Serializable {
    public String id;
    public String panelType;
    public int number;
    public String title;
    public int scale;
    public List<Coordinate> exterior;

    public Extent(JSONObject json) {
        id = JsonUtil.getString(json, "geographicElement.EX_BoundingPolygon.polygon.gml:Polygon.gml:id");
        //ChartOverlay.LOGGER.info("Extent: {}", id);

        Map<String, String> description = Parser.parseString(JsonUtil.getString(json, "description.gco:CharacterString"));
        panelType = description.get("panel type");
        number = Integer.parseInt(description.get("number"));
        title = description.get("title");
        scale = Integer.parseInt(description.get("scale"));
        exterior = new ArrayList<>();
        for (Object object : JsonUtil.getJsonArray(json, "geographicElement.EX_BoundingPolygon.polygon.gml:Polygon.gml:exterior.gml:LinearRing.gml:pos")) {
            exterior.add(Parser.parseCoordinate((String) object));
        }
    }
}
