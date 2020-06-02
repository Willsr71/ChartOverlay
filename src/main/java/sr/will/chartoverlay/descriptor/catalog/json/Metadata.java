package sr.will.chartoverlay.descriptor.catalog.json;

import org.json.JSONObject;
import sr.will.chartoverlay.util.JsonUtil;

import java.io.Serializable;

public class Metadata implements Serializable {
    public String title;
    public String date;
    public String description;
    public String organisation;

    public Metadata(JSONObject json) {
        title = JsonUtil.getString(json, "identificationInfo.MD_DataIdentification.citation.CI_Citation.title.gco:CharacterString");
        date = JsonUtil.getString(json, "identificationInfo.MD_DataIdentification.citation.CI_Citation.date.CI_Date.date.gco:DateTime");
        description = JsonUtil.getString(json, "identificationInfo.MD_DataIdentification.abstract.gco:CharacterString");
        organisation = JsonUtil.getString(json, "contact.CI_ResponsibleParty.organisationName.gco:CharacterString");
    }
}
