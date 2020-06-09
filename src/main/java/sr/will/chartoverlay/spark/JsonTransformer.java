package sr.will.chartoverlay.spark;

import spark.ResponseTransformer;
import sr.will.chartoverlay.ChartOverlay;

public class JsonTransformer implements ResponseTransformer {
    @Override
    public String render(Object model) {
        return ChartOverlay.GSON.toJson(model);
    }
}
