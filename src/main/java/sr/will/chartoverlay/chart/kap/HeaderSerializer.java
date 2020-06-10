package sr.will.chartoverlay.chart.kap;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;

public class HeaderSerializer implements JsonSerializer<Header> {

    @Override
    public JsonElement serialize(Header header, Type type, JsonSerializationContext jsc) {
        JsonElement serializedHeader = jsc.serialize(header.items);
        if (header.unknownItems.size() != 0)
            serializedHeader.getAsJsonObject().add("unknownItems", jsc.serialize(header.unknownItems));
        if (header.unknownItemsLists.size() != 0)
            serializedHeader.getAsJsonObject().add("unknownItemsLists", jsc.serialize(header.unknownItemsLists));

        // Put all colors in a single tree
        JsonObject obj = serializedHeader.getAsJsonObject();
        JsonObject colorObj = new JsonObject();
        Iterator<Map.Entry<String, JsonElement>> iterator = serializedHeader.getAsJsonObject().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonElement> entry = iterator.next();
            if (!entry.getKey().startsWith("_color")) continue;
            colorObj.add(entry.getKey().substring(6), entry.getValue());
            iterator.remove();
        }

        if (colorObj.size() != 0) obj.add("colors", colorObj);

        return serializedHeader;
    }
}
