package sr.will.chartoverlay.generic;

import com.google.gson.*;

import java.lang.reflect.Type;

public class PointDeserializer implements JsonDeserializer<ChartPoint> {

    @Override
    public ChartPoint deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        ChartPoint chartPoint = new ChartPoint();
        if (obj.has("x") && obj.has("y")) {
            chartPoint.setImage(obj.get("x").getAsInt(), obj.get("y").getAsInt());
        }

        if (obj.has("latitude") && obj.has("longitude")) {
            chartPoint.setGPS(obj.get("latitude").getAsDouble(), obj.get("longitude").getAsDouble());
        }

        return chartPoint;
    }
}
