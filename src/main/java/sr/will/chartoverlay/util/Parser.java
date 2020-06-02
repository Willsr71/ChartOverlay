package sr.will.chartoverlay.util;

import sr.will.chartoverlay.descriptor.catalog.json.Coordinate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {

    public static Coordinate parseCoordinate(String string) {
        String[] s = string.split(" ");
        return new Coordinate(Double.parseDouble(s[0]), Double.parseDouble(s[1]));
    }

    public static Map<String, String> parseString(String string) {
        Map<String, String> stringMap = new HashMap<>();
        List<String> split = Arrays.asList(string.split("; "));
        for (int i = 0; i < split.size(); i++) {
            String keyValue = split.get(i);

            // Check if the next string has a ";" in it but no ":", if so it is a name and should be put back together
            if (i + 1 < split.size()) {
                if (!split.get(i + 1).contains(": ")) {
                    // Append next string onto this one
                    keyValue += "; " + split.get(i + 1);
                    // Skip next string
                    i++;
                }
            }

            String[] kv = keyValue.split(": ");
            stringMap.put(kv[0], kv[1]);
        }
        return stringMap;
    }
}
