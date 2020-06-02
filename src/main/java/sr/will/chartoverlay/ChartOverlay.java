package sr.will.chartoverlay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sr.will.chartoverlay.descriptor.catalog.json.Catalog;
import sr.will.chartoverlay.spark.SparkHandler;
import sr.will.chartoverlay.util.FileUtil;

public class ChartOverlay {
    public static ChartOverlay instance;

    public static final Logger LOGGER = LoggerFactory.getLogger("ChartOverlay");
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
        catalog = FileUtil.readProductCatalog("RNCProdCat_19115");
        catalog.catalogCharts();
    }
}
