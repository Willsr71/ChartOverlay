package sr.will.chartoverlay.spark;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.ResponseTransformer;
import sr.will.chartoverlay.chart.generic.ChartPoint;
import sr.will.chartoverlay.chart.generic.PointSerializer;

public class JsonTransformer implements ResponseTransformer {
    private static Gson gson = new GsonBuilder()
                                       .registerTypeAdapter(ChartPoint.class, new PointSerializer())
                                       .create();

    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }
}
