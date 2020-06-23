package sr.will.chartoverlay.spark;

import spark.Spark;
import sr.will.chartoverlay.ChartOverlay;
import sr.will.chartoverlay.chart.ChartIO;

import static spark.Spark.*;

public class SparkHandler {
    private static final JsonTransformer JSON_TRANSFORMER = new JsonTransformer();

    public SparkHandler() {
        port(8080);
        externalStaticFileLocation(ChartOverlay.config.webDir);
    }

    public void start() {
        before((q, a) -> {
            String path = q.pathInfo();
            if (path.endsWith("/")) a.redirect(path.substring(0, path.length() - 1));
        });

        path("/catalog", () -> {
            get("/list", (q, a) -> ChartOverlay.catalog.chartsByNumber.keySet(), JSON_TRANSFORMER);
            get("/:chart", (q, a) -> ChartOverlay.catalog.chartsByNumber.get(q.params(":chart")), JSON_TRANSFORMER);
            path("/district", () -> {
                get("/list", (q, a) -> ChartOverlay.catalog.chartsByDistrict.keySet(), JSON_TRANSFORMER);
                get("/:district", (q, a) -> ChartOverlay.catalog.chartsByDistrict.get(Integer.parseInt(q.params(":district"))), JSON_TRANSFORMER);
            });
            path("/state", () -> {
                get("/list", (q, a) -> ChartOverlay.catalog.chartsByState.keySet(), JSON_TRANSFORMER);
                get("/:state", (q, a) -> ChartOverlay.catalog.chartsByState.get(q.params(":state")), JSON_TRANSFORMER);
            });
            path("/region", () -> {
                get("/list", (q, a) -> ChartOverlay.catalog.chartsByRegion.keySet(), JSON_TRANSFORMER);
                get("/:region", (q, a) -> ChartOverlay.catalog.chartsByRegion.get(Integer.parseInt(q.params(":region"))), JSON_TRANSFORMER);
            });
            before((q, a) -> a.type("application/json"));
        });

        path("/chart", () -> {
            before("/:chart", (q, a) -> a.type("application/json"));
            get("/:chart", (q, a) -> ChartIO.getChart(q.params(":chart")), JSON_TRANSFORMER);
        });
    }

    public void stop() {
        Spark.stop();
    }
}
