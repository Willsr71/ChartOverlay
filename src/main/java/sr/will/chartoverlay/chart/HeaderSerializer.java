package sr.will.chartoverlay.chart;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class HeaderSerializer implements JsonSerializer<Header> {

    @Override
    public JsonElement serialize(Header header, Type type, JsonSerializationContext jsc) {
        JsonElement serializedHeader = jsc.serialize(header.items);
        if (header.unknownItems.size() != 0)
            serializedHeader.getAsJsonObject().add("unknownItems", jsc.serialize(header.unknownItems));
        if (header.unknownItemsLists.size() != 0)
            serializedHeader.getAsJsonObject().add("unknownItemsLists", jsc.serialize(header.unknownItemsLists));
        return serializedHeader;
    }
}
