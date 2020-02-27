package KNN;

import java.util.*;

public abstract class HandleNeighbors {

    LinkedHashMap<Integer, Double> delimit_kNearestNeighbors(HashMap<Integer, Double> allNearestNeighbors, int k) {
        // Sort all values similarity-scores (Highest first)
        allNearestNeighbors = sortByValue(allNearestNeighbors);

        // Declare new LinkedHashMap to store the k nearest neighbors
        LinkedHashMap<Integer, Double> kNearestNeighbors = new LinkedHashMap<>();

        // Loop through and assign the k highest similarities to kNearestNeighbors
        for (Map.Entry<Integer, Double> entry : allNearestNeighbors.entrySet()) {
            kNearestNeighbors.put(entry.getKey(), entry.getValue());

            // Break the loop as soon as the k highest similarities are found
            if (--k == 0)
                break;
        }

        return kNearestNeighbors;
    }

    private LinkedHashMap<Integer, Double> sortByValue(HashMap<Integer, Double> unsortedHashMap) {
        // Initializing linked list to be a list of the unsorted hash-map
        LinkedList<HashMap.Entry<Integer, Double>> list = new LinkedList<>(unsortedHashMap.entrySet());

        // Sort list based on custom comparator by value
        list.sort((o1, o2) -> (o2.getValue()).compareTo(o1.getValue()));

        LinkedHashMap<Integer, Double> sortedMap = new LinkedHashMap<>();

        // Iterate through the sorted list and mirroring it to a map
        for (Map.Entry<Integer, Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
}
