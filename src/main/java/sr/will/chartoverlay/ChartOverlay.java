package sr.will.chartoverlay;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sr.will.chartoverlay.chart.kap.Header;
import sr.will.chartoverlay.chart.kap.HeaderSerializer;
import sr.will.chartoverlay.descriptor.catalog.json.Catalog;
import sr.will.chartoverlay.generic.ChartPoint;
import sr.will.chartoverlay.generic.PointSerializer;
import sr.will.chartoverlay.spark.SparkHandler;
import sr.will.chartoverlay.util.FileUtil;

import java.io.File;

public class ChartOverlay {
    public static ChartOverlay instance;

    public static final Logger LOGGER = LoggerFactory.getLogger("ChartOverlay");
    public static final Gson GSON = new GsonBuilder()
                                            .registerTypeAdapter(ChartPoint.class, new PointSerializer())
                                            .registerTypeAdapter(Header.class, new HeaderSerializer())
                                            .create();

    public static SparkHandler sparkHandler;

    public static Catalog catalog;

    public ChartOverlay() {
        instance = this;
        sparkHandler = new SparkHandler();

        reload();

        LOGGER.info("Done!");
    }

    public void stop() {
        LOGGER.info("Stopping!");

        sparkHandler.stop();

        System.exit(0);
    }

    public void reload() {
        sparkHandler.stop();
        sparkHandler.start();
        reloadCatalog();
    }

    public void reloadCatalog() {
        catalog = FileUtil.readJson(new File("catalog", "RNCProdCat_19115.json"), Catalog.class);
        catalog.catalogCharts();
    }
}
