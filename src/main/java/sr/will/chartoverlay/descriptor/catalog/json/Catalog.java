package sr.will.chartoverlay.descriptor.catalog.json;

import org.json.JSONObject;
import sr.will.chartoverlay.ChartOverlay;
import sr.will.chartoverlay.util.JsonUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Catalog implements Serializable {
    public Metadata metadata;
    public List<Chart> charts;

    public transient Map<String, Chart> chartsByNumber;
    public transient Map<Integer, List<String>> chartsByDistrict;
    public transient Map<String, List<String>> chartsByState;
    public transient Map<Integer, List<String>> chartsByRegion;

    public Catalog(JSONObject json) {
        metadata = new Metadata(JsonUtil.getJsonObject(json, "seriesMetadata.MD_Metadata"));
        charts = new ArrayList<>();
        for (Object object : JsonUtil.getJsonArray(json, "composedOf")) {
            charts.add(new Chart(JsonUtil.getJsonObject((JSONObject) object, "DS_DataSet.has.MD_Metadata")));
        }

        catalogCharts();
    }

    public void catalogCharts() {
        ChartOverlay.LOGGER.info("Cataloging charts...");

        chartsByNumber = new HashMap<>();
        chartsByDistrict = new HashMap<>();
        chartsByState = new HashMap<>();
        chartsByRegion = new HashMap<>();

        for (Chart chart : charts) {
            chartsByNumber.put(chart.chart, chart);
            for (Integer district : chart.keywords.districts) {
                if (!chartsByDistrict.containsKey(district)) chartsByDistrict.put(district, new ArrayList<>());
                chartsByDistrict.get(district).add(chart.chart);
            }
            for (String state : chart.keywords.states) {
                if (!chartsByState.containsKey(state)) chartsByState.put(state, new ArrayList<>());
                chartsByState.get(state).add(chart.chart);
            }
            for (Integer region : chart.keywords.regions) {
                if (!chartsByRegion.containsKey(region)) chartsByRegion.put(region, new ArrayList<>());
                chartsByRegion.get(region).add(chart.chart);
            }
        }
    }
}
