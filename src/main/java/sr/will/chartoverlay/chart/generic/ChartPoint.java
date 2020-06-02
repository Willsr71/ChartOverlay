package sr.will.chartoverlay.chart.generic;

public class ChartPoint {
    public transient PointType type;
    public int x;
    public int y;
    public double latitude;
    public double longitude;

    public ChartPoint(int x, int y, double latitude, double longitude) {
        setImage(x, y);
        setGPS(latitude, longitude);
        this.type = PointType.BOTH;
    }

    public ChartPoint(int x, int y) {
        setImage(x, y);
        this.type = PointType.IMAGE;
    }

    public ChartPoint(double latitude, double longitude) {
        setGPS(latitude, longitude);
        this.type = PointType.GPS;
    }

    public void setImage(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setGPS(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
