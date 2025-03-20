package tools;

import java.util.Collections;
import java.util.List;

public class DistanceSorter {
    public static void quickSort(List<DistanceResult> list, int start, int end) {
        if (start < end) {
            int pivotIndex = partition(list, start, end);
            //Sort left part
            quickSort(list, start, pivotIndex - 1);
            //Sort right part
            quickSort(list, pivotIndex + 1, end);
        }
    }

    private static int partition(List<DistanceResult> list, int start, int end) {
        //Last element set as pivot
        double pivot = list.get(end).getDistance();
        int i = start - 1;

        for (int j = start; j < end; j++) {
            //Swap if element is smaller than pivot
            if (list.get(j).getDistance() < pivot) {
                i++;
                Collections.swap(list, i, j);
            }
        }
        // Adjust pivot
        Collections.swap(list, i + 1, end);
        return i + 1;
    }

}
