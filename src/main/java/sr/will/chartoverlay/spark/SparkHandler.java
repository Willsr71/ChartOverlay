package sr.will.chartoverlay.spark;

import spark.Spark;
import sr.will.chartoverlay.ChartOverlay;
import sr.will.chartoverlay.chart.ChartManager;
import sr.will.chartoverlay.util.FileUtil;

import java.io.File;

import static spark.Spark.*;

public class SparkHandler {
    public SparkHandler() {
        port(8080);
        externalStaticFileLocation("web");
        defaultResponseTransformer(new JsonTransformer());
    }

    public void start() {
        File uploadDir = new File("upload");
        uploadDir.mkdir();

        before((q, a) -> {
            String path = q.pathInfo();
            if (path.endsWith("/")) a.redirect(path.substring(0, path.length() - 1));
        });

        path("/catalog", () -> {
            get("/list", (q, a) -> ChartOverlay.catalog.chartsByNumber.keySet());
            get("/:chart", (q, a) -> ChartOverlay.catalog.chartsByNumber.get(q.params(":chart")));
            path("/district", () -> {
                get("/list", (q, a) -> ChartOverlay.catalog.chartsByDistrict.keySet());
                get("/:district", (q, a) -> ChartOverlay.catalog.chartsByDistrict.get(Integer.parseInt(q.params(":district"))));
            });
            path("/state", () -> {
                get("/list", (q, a) -> ChartOverlay.catalog.chartsByState.keySet());
                get("/:state", (q, a) -> ChartOverlay.catalog.chartsByState.get(q.params(":state")));
            });
            path("/region", () -> {
                get("/list", (q, a) -> ChartOverlay.catalog.chartsByRegion.keySet());
                get("/:region", (q, a) -> ChartOverlay.catalog.chartsByRegion.get(Integer.parseInt(q.params(":region"))));
            });
        });

        path("/chart", () -> {
            get("/:chart", (q, a) -> ChartManager.fetchBSB(q.params(":chart")));
        });

        get("/update", (request, response) -> {
            FileUtil.processProductCatalog("RNCProdCat_19115");
            ChartOverlay.instance.reloadCatalog();
            return new Response();
        });

        get("/stop", (request, response) -> {
            ChartOverlay.instance.stop();
            return new Response();
        });
    }

    public void stop() {
        Spark.stop();
    }
}
