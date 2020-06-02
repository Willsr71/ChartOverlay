package sr.will.chartoverlay.descriptor.catalog.json;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Coordinate implements Serializable {
    @SerializedName("lat")
    public double latitude;
    @SerializedName("long")
    public double longitude;

    public Coordinate(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String toString() {
        return "Coordinate [lat=" + latitude +
                       ", long=" + longitude +
                       "]";
    }
}
