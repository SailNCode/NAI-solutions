package tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class VecReader {
    public static List<Vec> readVectors(Path filePath) throws IOException {
        List<Vec> vectors = new ArrayList<>();
        Files.lines(filePath).forEach(line -> {
            vectors.add(parseVector(line, true));
        });
        return vectors;
    }
    public static Vec parseVector(String input, boolean withCategory) throws NumberFormatException{
        String[] split = input.split(",");
        double[] coordinates = new double[split.length - (withCategory ? 1 : 0)];
        for (int i = 0; i < split.length - (withCategory ? 1 : 0); i++) {
            coordinates[i] = Double.parseDouble(split[i]);
        }
        return new Vec((withCategory ? split[split.length - 1] : null), coordinates);
    }
    public static Vec parseVector(String input, boolean withCategory, int noCoordinates) {
        if (input.split(",").length - (withCategory ? 1 : 0) != noCoordinates) {
            throw new IllegalArgumentException();
        }
        return parseVector(input, withCategory);
    }
}
