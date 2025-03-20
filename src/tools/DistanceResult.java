package tools;

public class DistanceResult {
    private double distance;
    private String category;

    public DistanceResult(double distance, String category) {
        this.distance = distance;
        this.category = category;
    }
    public String toString() {
        return "d=" + distance + " cat=" + category;
    }

    public String getCategory() {
        return category;
    }

    public double getDistance() {
        return distance;
    }
}
