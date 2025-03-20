package pro_1_kNN;

import tools.Vec;
import tools.VecReader;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        final String TEXT_FILES_PATH = "src/pro_1_kNN/text_files/";
        final String TRAIN_VECTORS_PATH = "iris_train.txt";
        final String TEST_VECTORS_PATH = "iris_test.txt";
        List<Vec> trainVectors = VecReader.readVectors(Paths.get(TEXT_FILES_PATH + TRAIN_VECTORS_PATH));

        Predicate<Integer> inRange = k -> k > 0 && k <= trainVectors.size();

        while (true) {
            //Prompting for k parameter:
            int k = 0;
            do {
                try {
                    String kInput = JOptionPane.showInputDialog("Input the k parameter in range [1, %d]".formatted(trainVectors.size()));
                    if (kInput == null) {
                        System.exit(0);
                    }
                    k = Integer.parseInt(kInput);
                } catch (NumberFormatException exc) {
                    System.out.println("Not an int!");
                } catch (IllegalArgumentException exc) {
                    System.out.println("Wrong k parameter");
                }
            } while (!inRange.test(k));

            //Model initialization:
            KNNModel kNNModel = new KNNModel(trainVectors, k);

            //Prompting for general mode:
            Mode[] generalModes = {Mode.TEST, Mode.CLASSIFY};
            int generalModeInput = JOptionPane.showOptionDialog(
                    null,
                    "Select the mode",
                    "Mode selection",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    generalModes,
                    1
            );
            if (generalModeInput == -1) {
                System.exit(0);
            }

            switch(generalModeInput) {
                //Testing mode:
                case 0 -> {
                    List<Vec> vectorsToTest;
                    String userFilePath = null;
                    Path path = null;
                    do {
                        userFilePath = JOptionPane.showInputDialog("Enter file path:", TEXT_FILES_PATH + TEST_VECTORS_PATH);
                        if (userFilePath == null) {
                            System.exit(0);
                        }
                        path = Paths.get(userFilePath);

                    } while (!Files.isRegularFile(path));
                    try {
                        vectorsToTest = VecReader.readVectors(path);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    kNNModel.testVectors(vectorsToTest);
                }
                //Classifying mode:
                case 1 -> {
                    List<Vec> vectorsToClassify = new ArrayList<>();
                    String format = Arrays.stream(trainVectors.get(0).getCoordinates())
                                    .mapToObj(String::valueOf)
                                    .collect(Collectors.joining(","));
                    String userInput = null;
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
                    vectorsToClassify.forEach(kNNModel::classifyVector);
                }
            }
        }
    }
}
