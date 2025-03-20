package pro_2_perceptron;
import tools.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

public class Perceptron {
    private int size;
    private Vec initialWeightVector;
    private Vec weightVector;
    private double alfa, theta;
    private HashMap<String, Integer> categories;
    public Perceptron(Vec weightVector, double alpha, double theta) {
        size = weightVector.getCoordinates().length;
        this.initialWeightVector = this.weightVector = weightVector;
        this.alfa = alpha;
        this.theta = theta;
        categories = new HashMap<>();
    }

    @Override
    public String toString() {
        return String.format("Perceptron: %n" +
                "vector size = %d %n" +
                "initial weight vector: %s %n" +
                "weight vector: %s %n" +
                "alfa = %.2f %n" +
                "theta = %.2f %n" +
                "categories = %s %n",
                size, initialWeightVector.toString(), weightVector.toString(), alfa, theta, categories);
    }

    private Integer getNumCategory(String stringCategory) {
        return categories.get(stringCategory);
    }
    private String getStringCategory(int intCategory) throws NoSuchElementException {
        return categories.entrySet().stream()
                .filter(k -> k.getValue() == intCategory)
                .findFirst()
                .orElseThrow()
                .getKey();
    }
    public HashMap<String, Integer> getCategories() {
        return categories;
    }
    public int classify(Vec inputVector) {
        if (size != inputVector.getSize())
            throw new IllegalArgumentException("Length of vectors should be equal");
        double scalarProduct = weightVector.scalarProduct(inputVector);
        boolean thetaTest = scalarProduct >= theta;
        int result = weightVector.scalarProduct(inputVector) >= theta ? 1 : 0;
        System.out.printf("Classification: %.2f >= %.2f = %b%n", scalarProduct, theta, thetaTest);
        String stringCategory = null;
        try {
            stringCategory = getStringCategory(result);
        } catch (NoSuchElementException exc) {}
        System.out.println("\tResult: " +  result + (stringCategory == null ? "" : " (" + stringCategory + ")"));
        return result;
    }
    public void train(Vec inputVector) {
        if (!categories.containsKey(inputVector.getCategory())) {
            if (categories.size() == 2) {
                throw new IllegalArgumentException("Only two categories of input vector allowed!");
            } else {
                categories.put(inputVector.getCategory(), categories.size());
            }
        }

        if (size != inputVector.getSize())
            throw new IllegalArgumentException("Length of vectors should be equal");

        System.out.println("train(...) invoked with vector: " + inputVector);
        int deducedCategory = classify(inputVector);
        if (deducedCategory!= getNumCategory(inputVector.getCategory())) {
            System.out.printf("Wrong classification (d=%d, y=%d). Model redefining...%n", getNumCategory(inputVector.getCategory()), deducedCategory);
            //Updating the model:
            System.out.printf("\tWeight vector change: %s -> ", Arrays.toString(weightVector.getCoordinates()));
            weightVector = calcWeightVector(inputVector, deducedCategory);
            System.out.println(Arrays.toString(weightVector.getCoordinates()));

            System.out.printf("\tTheta change: %.2f -> ", theta);
            theta = calcTheta(inputVector, deducedCategory);
            System.out.println(theta);
        } else {
            System.out.printf("Correct classification: (d=%d, y=%d)%n", getNumCategory(inputVector.getCategory()), deducedCategory);
        }
        System.out.println();
    }

    public Vec calcWeightVector(Vec inputVector, int deducedCategory) {
        int d = getNumCategory(inputVector.getCategory()),
                y = deducedCategory;
        return weightVector.add(
                inputVector.multiplyBy((d - y) * alfa));
    }
    public double calcTheta(Vec inputVector, int deducedCategory) {
        int d = getNumCategory(inputVector.getCategory()),
                y = deducedCategory;
        return theta - (d - y) * alfa;
    }

    public void test(List<Vec> vectorsToTest) {
        AtomicInteger nPassedTests = new AtomicInteger(0);
        vectorsToTest.forEach(vec -> {
            int deducedCategory = classify(vec);
            boolean testPassed = deducedCategory == getNumCategory(vec.getCategory());
            if (testPassed) {
                nPassedTests.incrementAndGet();
            }
            System.out.println("\tTest passed: " + testPassed);
        });
        System.out.printf("Total accuracy: %.2f%%%n", (double) nPassedTests.get() / vectorsToTest.size() * 100);
    }
}
