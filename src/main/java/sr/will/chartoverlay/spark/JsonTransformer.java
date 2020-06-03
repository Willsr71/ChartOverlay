package sr.will.chartoverlay.spark;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.ResponseTransformer;
import sr.will.chartoverlay.chart.Header;
import sr.will.chartoverlay.chart.HeaderSerializer;
import sr.will.chartoverlay.generic.ChartPoint;
import sr.will.chartoverlay.generic.PointSerializer;

public class JsonTransformer implements ResponseTransformer {
    private static final Gson gson = new GsonBuilder()
                                       .registerTypeAdapter(ChartPoint.class, new PointSerializer())
                                       .registerTypeAdapter(Header.class, new HeaderSerializer())
                                       .create();

    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }
}
