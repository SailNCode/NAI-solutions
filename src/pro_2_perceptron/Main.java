package pro_2_perceptron;

import tools.*;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;


public class Main {
    public static void main(String[] args) {
        final String TEXT_FILES_PATH = "src/pro_2_perceptron/text_files/";
        final String TRAIN_PATH = "iris_train.txt";
        final String TEST_PATH = "iris_test.txt";

        //Reading vectors:
        List<Vec> trainVectors = null;
        try {
            trainVectors = VecReader.readVectors(Paths.get(TEXT_FILES_PATH + TRAIN_PATH));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Initializing perceptron:
        Random random = new Random();
        double[] coordinates = DoubleStream.generate(() -> random.nextDouble(5)).limit(trainVectors.getFirst().getCoordinates().length).toArray();
        Vec weightVector = new Vec(coordinates);
        Double alpha = null;
        Double theta = null;
        do {
            try {
                alpha = Double.parseDouble(JOptionPane.showInputDialog("\uD835\uDEFC:"));
                theta = Double.parseDouble(JOptionPane.showInputDialog("Θ:"));
            } catch (NumberFormatException e) {
            }
        } while (alpha == null || theta == null);
        Perceptron perceptron = new Perceptron(weightVector, alpha, theta);

        //Training perceptron:
        trainVectors.forEach(perceptron::train);

        while (true) {
            //Prompting for choosing mode:
            String[] mainOptions = {"TEST", "CLASSIFY", "INFO"};
            int input = JOptionPane.showOptionDialog(
                    null,
                    "Choose the option",
                    "Option selection",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    mainOptions,
                    0
                    );
            switch(input) {
                //Exiting
                case -1 -> System.exit(0);
                //Testing mode
                case 0 -> {
                    List<Vec> vectorsToTest;
                    String userFilePath = null;
                    Path path = null;
                    do {
                        userFilePath = JOptionPane.showInputDialog("Enter file path:",  (TEXT_FILES_PATH + TEST_PATH));
                        if (userFilePath == null) {
                            break;
                        }
                        path = Paths.get(userFilePath);
                    } while (!Files.isRegularFile(path));
                    try {
                        vectorsToTest = VecReader.readVectors(path);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    perceptron.test(vectorsToTest);
                }
                //Classifying mode
                case 1 -> {
                    List<Vec> vectorsToClassify = new ArrayList<>();
                    String userInput = null;
                    Vec patternVec = trainVectors.getFirst();
                    String format = Arrays.stream(patternVec.getCoordinates())
                            .mapToObj(Double::toString)
                            .collect(Collectors.joining(","));
                    while (true) {
                        try {
                            userInput = JOptionPane.showInputDialog(
                                    ("Format: %s%n" +
                                            "Input vector coordinates or close window to obtain result").formatted(format), format);
                            if (userInput == null) {
                                break;
                            }
                            Vec newVec = VecReader.parseVector(userInput, false, trainVectors.getFirst().getCoordinates().length);
                            vectorsToClassify.add(newVec);
                            System.out.printf("Vector (%s) successfully added to classify.%n", newVec);
                        } catch (NumberFormatException exc) {
                            System.out.println("Wrong format!");
                        } catch (IllegalArgumentException exc) {
                            System.out.println("Wrong number of coordinates!");
                        }
                    }
                    vectorsToClassify.forEach(perceptron::classify);
                }
                case 2 -> {
                    JOptionPane.showMessageDialog(null, perceptron, "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }
}