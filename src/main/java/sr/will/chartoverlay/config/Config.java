package sr.will.chartoverlay.config;

import java.util.HashMap;

public class Config {
    public int productCatalogNum = 19115;
    public long lastCatalogFetch = 0;
    public long catalogFetchInterval = 1000 * 60 * 60 * 24; // One day
    public String webDir = "web";
}
