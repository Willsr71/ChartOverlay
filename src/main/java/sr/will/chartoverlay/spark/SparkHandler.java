package sr.will.chartoverlay.spark;

import spark.Spark;
import sr.will.chartoverlay.ChartOverlay;
import sr.will.chartoverlay.chart.ChartIO;
import sr.will.chartoverlay.chart.ChartManager;
import sr.will.chartoverlay.util.FileUtil;

import static spark.Spark.*;

public class SparkHandler {
    private static final JsonTransformer JSON_TRANSFORMER = new JsonTransformer();

    public SparkHandler() {
        port(8080);
        externalStaticFileLocation("web");
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
        });

        path("/chart", () -> {
            get("/:chart", (q, a) -> ChartManager.fetchBSB(q.params(":chart")), JSON_TRANSFORMER);
            get("/png/:extent", (q, a) -> {
                a.raw();
                a.header("Content-Type", "image/png");
                return ChartIO.getExtentImage(Integer.parseInt(q.params(":extent")));
            });
        });

        get("/update", (request, response) -> {
            FileUtil.processProductCatalog("RNCProdCat_19115");
            ChartOverlay.instance.reloadCatalog();
            return new Response();
        }, JSON_TRANSFORMER);
    }

    public void stop() {
        Spark.stop();
    }
}
