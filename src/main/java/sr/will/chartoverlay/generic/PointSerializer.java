package sr.will.chartoverlay.generic;

import com.google.gson.*;

import java.lang.reflect.Type;

public class PointSerializer implements JsonSerializer<ChartPoint> {

    @Override
    public JsonElement serialize(ChartPoint chartPoint, Type type, JsonSerializationContext jsc) {
        JsonObject jsonObject = (JsonObject) new GsonBuilder().create().toJsonTree(chartPoint);
        if (chartPoint.type == PointType.GPS) {
            jsonObject.remove("x");
            jsonObject.remove("y");
        } else if (chartPoint.type == PointType.IMAGE) {
            jsonObject.remove("latitude");
            jsonObject.remove("longitude");
        }

        return jsonObject;
    }
}
