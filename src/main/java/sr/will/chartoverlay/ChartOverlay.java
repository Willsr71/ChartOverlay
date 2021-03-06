package sr.will.chartoverlay;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sr.will.chartoverlay.chart.kap.Header;
import sr.will.chartoverlay.chart.kap.HeaderSerializer;
import sr.will.chartoverlay.config.Config;
import sr.will.chartoverlay.descriptor.catalog.json.Catalog;
import sr.will.chartoverlay.generic.ChartPoint;
import sr.will.chartoverlay.generic.PointDeserializer;
import sr.will.chartoverlay.generic.PointSerializer;
import sr.will.chartoverlay.spark.SparkHandler;
import sr.will.chartoverlay.util.FileUtil;

public class ChartOverlay {
    public static ChartOverlay instance;

    public static final Logger LOGGER = LoggerFactory.getLogger("ChartOverlay");
    public static final Gson GSON = new GsonBuilder()
                                            .registerTypeAdapter(ChartPoint.class, new PointSerializer())
                                            .registerTypeAdapter(ChartPoint.class, new PointDeserializer())
                                            .registerTypeAdapter(Header.class, new HeaderSerializer())
                                            .create();

    public static SparkHandler sparkHandler;

    public static Config config;
    public static Catalog catalog;

    public ChartOverlay() {
        instance = this;

        reload();

        sparkHandler = new SparkHandler();
        sparkHandler.start();

        LOGGER.info("Done!");
    }

    public void stop() {
        LOGGER.info("Stopping!");

        sparkHandler.stop();

        System.exit(0);
    }

    public void reload() {
        if (sparkHandler != null) {
            sparkHandler.stop();
            sparkHandler.start();
        }

        config = FileUtil.getConfig();

        catalog = FileUtil.getProductCatalog();
    }

    public void saveConfig() {
        FileUtil.writeGson(config, ".", "config", "config");
    }
}
