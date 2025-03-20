package tools;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Vec {
    private final double[] coordinates;
    private final int size;
    private final String category;

    public Vec(String category, double... coordinates) {
        this.coordinates = coordinates;
        this.size = coordinates.length;
        this.category = category;
    }
    public Vec(double... coordinates) {
        this.coordinates = coordinates;
        this.size = coordinates.length;
        this.category = null;
    }

    @Override
    public String toString() {
        String formattedcoordinates = "[" + Arrays.stream(coordinates).mapToObj(doble -> String.format("%.2f", doble)).collect(Collectors.joining(", ")) + "]";
        return formattedcoordinates + (category != null ? (", category=" + category ): "");
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public int getSize() {
        return size;
    }

    public String getCategory() {
        if (category == null) {
            throw new IllegalStateException("Vector has no category!");
        }
        return category;
    }
    public double distanceTo(Vec otherVec) {
        double[] otherVecCoords = otherVec.getCoordinates();
        if (coordinates.length != otherVecCoords.length) throw new RuntimeException("Length of vectors should be equal");
        double sum = 0;
        for (int i = 0; i < coordinates.length; i++) {
            sum += Math.pow(otherVecCoords[i] - coordinates[i], 2);
        }
        return Math.sqrt(sum);
    }
    public double scalarProduct(Vec otherVec) {
        double[] otherVecCoords = otherVec.getCoordinates();
        if (coordinates.length != otherVecCoords.length)
            throw new IllegalArgumentException("Length of vectors should be equal");
        double scalarProduct = 0;
        for (int i = 0; i < coordinates.length; i++) {
            scalarProduct += coordinates[i] * otherVecCoords[i];
        }
        return scalarProduct;
    }

    public Vec multiplyBy(double constant) {
        double[] newCoords = new double[coordinates.length];
        for (int i = 0; i < newCoords.length; i++) {
            newCoords[i] = coordinates[i] * constant;
        }
        return new Vec(category, newCoords);
    }
    public Vec add(Vec otherVec) {
        double[] otherVecCoords = otherVec.getCoordinates();
        double[] newCoordinates = new double[coordinates.length];
        for (int i = 0; i < newCoordinates.length; i++) {
            newCoordinates[i] = coordinates[i] + otherVecCoords[i];
        }
        return new Vec(null, newCoordinates);
    }
}
