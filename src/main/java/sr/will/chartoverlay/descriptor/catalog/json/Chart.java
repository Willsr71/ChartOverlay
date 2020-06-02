package sr.will.chartoverlay.descriptor.catalog.json;

import org.json.JSONArray;
import org.json.JSONObject;
import sr.will.chartoverlay.ChartOverlay;
import sr.will.chartoverlay.util.JsonUtil;
import sr.will.chartoverlay.util.Parser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Chart implements Serializable {
    public String chart;
    public String title;
    public Edition edition;
    public String revisionDate;
    public String publishDate;
    public List<Extent> extents;
    public Keywords keywords;

    public Chart(JSONObject json) {
        // Chart number is usually an integer but can occasionally be a string (1114A)
        Object chartNum = JsonUtil.getObject(json, "identificationInfo.MD_DataIdentification.citation.CI_Citation.title.gco:CharacterString");
        if (chartNum instanceof Integer) chart = ((Integer) chartNum).toString();
        else chart = (String) chartNum;
        //ChartOverlay.LOGGER.info("Chart {}", chart);

        title = JsonUtil.getString(json, "identificationInfo.MD_DataIdentification.citation.CI_Citation.alternateTitle.gco:CharacterString");

        // Editions
        edition = new Edition(Parser.parseString(JsonUtil.getString(json, "identificationInfo.MD_DataIdentification.citation.CI_Citation.edition.gco:CharacterString")));

        // Dates
        for (Object object : JsonUtil.getJsonArray(json, "identificationInfo.MD_DataIdentification.citation.CI_Citation.date")) {
            JSONObject dateSection = ((JSONObject) object).getJSONObject("CI_Date");
            String type = JsonUtil.getString(dateSection, "dateType.CI_DateTypeCode.content");
            String date = JsonUtil.getString(dateSection, "date.gco:Date");
            if (type.equals("revision")) {
                revisionDate = date;
            } else if (type.equals("publication")) {
                publishDate = date;
            }
        }

        // Extents
        Object extent = JsonUtil.getObject(json, "identificationInfo.MD_DataIdentification.extent");
        if (extent instanceof JSONObject) {
            extents = Collections.singletonList(new Extent(((JSONObject) extent).getJSONObject("EX_Extent")));
        } else if (extent instanceof JSONArray) {
            extents = new ArrayList<>();
            for (Object object : (JSONArray) extent) {
                extents.add(new Extent(((JSONObject) object).getJSONObject("EX_Extent")));
            }
        }

        // Keywords
        keywords = new Keywords();
        for (Object object : JsonUtil.getJsonArray(json, "identificationInfo.MD_DataIdentification.descriptiveKeywords.MD_Keywords.keyword")) {
            keywords.addKeyword(((JSONObject) object).getString("gco:CharacterString"));
        }

        // Check the maintenance code, we don't do anything with it though
        String maintenanceCode = JsonUtil.getString(json, "identificationInfo.MD_DataIdentification.resourceMaintenance.MD_MaintenanceInformation.maintenanceAndUpdateFrequency.MD_MaintenanceFrequencyCode.content");
        if (!maintenanceCode.equals("continual")) {
            ChartOverlay.LOGGER.warn("Chart {} has maint code of {}", chart, maintenanceCode);
        }
    }

    public static class Edition {
        public int source;
        public int raster;
        public int ntm;

        public Edition(Map<String, String> stringMap) {
            source = Integer.parseInt(stringMap.get("source edition"));
            raster = Integer.parseInt(stringMap.get("raster edition"));
            ntm = Integer.parseInt(stringMap.get("ntm edition"));
        }
    }

    public static class Keywords {
        public List<Integer> districts;
        public List<String> states;
        public List<Integer> regions;

        public Keywords() {
            districts = new ArrayList<>();
            states = new ArrayList<>();
            regions = new ArrayList<>();
        }

        public void addKeyword(String keyword) {
            String[] kv = keyword.split(": ");
            if (kv[0].equals("coast guard district")) {
                districts.add(Integer.parseInt(kv[1]));
            } else if (kv[0].equals("state")) {
                states.add(kv[1]);
            } else if (kv[0].equals("region")) {
                regions.add(Integer.parseInt(kv[1]));
            } else {
                ChartOverlay.LOGGER.warn("Unknown keyword type: {}", keyword);
            }
        }
    }
}
