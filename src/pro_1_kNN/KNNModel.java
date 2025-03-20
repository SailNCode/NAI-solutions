package pro_1_kNN;

import tools.DistanceResult;
import tools.DistanceSorter;
import tools.Vec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class KNNModel {
    private List<Vec> trainVectors;
    private int k;
    private HashMap<String, Integer> categoryCount = new HashMap<>();
    public KNNModel(List<Vec> trainVectors, int k) {
        this.trainVectors = trainVectors;
        this.k = k;
        //Loading category if unknown
        trainVectors.forEach(vec -> categoryCount.putIfAbsent(vec.getCategory(), 0));
    }
    public String classifyVector(Vec vector) {
        System.out.println("Classified vector: " + vector);

        List<DistanceResult> distanceResults = trainVectors.stream()
                .map(vec ->
                        new DistanceResult(
                                vec.distanceTo(vector),
                                vec.getCategory()))
                .collect(Collectors.toList());

        DistanceSorter.quickSort(distanceResults, 0, distanceResults.size() - 1);
        distanceResults = distanceResults.stream()
                .limit(k)
                .collect(Collectors.toList());;

        System.out.println("\tClosest vectors: " + distanceResults);
        distanceResults.forEach(res -> {
            categoryCount.computeIfPresent(
                    res.getCategory(),
                    (str, integer) -> integer + 1);
        });
        String deducedCategory = categoryCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow()
                .getKey();
        System.out.println("\tDeduced category: " + deducedCategory);

        //Cleaning category count
        for (String key : categoryCount.keySet()) {
            categoryCount.put(key, 0);
        }
        return deducedCategory;
    }
    public void testVectors(List<Vec> vectors) {
        AtomicInteger nPassedTests = new AtomicInteger(0);
        vectors.forEach(vec -> {
            String deducedCategory = classifyVector(vec);
            boolean testPassed = deducedCategory.equals(vec.getCategory());
            if (testPassed) {
                nPassedTests.incrementAndGet();
            }
            System.out.println("\tTest passed: " + testPassed);
        });
        System.out.printf("Total accuracy: %.2f%%%n", (double) nPassedTests.get() / vectors.size() * 100);
    }
}
