package KNN;

import Profiles.Profile;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;


public class EuclideanDistance extends HandleNeighbors {
    public void findNearestNeighbors(HashMap<Integer, Profile> userProfileHashMap, int k) throws IOException {
        LinkedHashMap<Integer, Double> nearestUsers = new LinkedHashMap<>();
        double[] fromUserSrc, toUserSrc;
        double similarity;
        int counter;

        DecimalFormat df = new DecimalFormat("0.0000", DecimalFormatSymbols.getInstance(Locale.US));

        String newFilePath = "data/euclideanNearestNeighbors.csv";

        // Prepare printWriter to write in specified file.
        FileWriter fileWriter = new FileWriter(newFilePath, false);
        PrintWriter printWriter = new PrintWriter(fileWriter);


        for (Map.Entry<Integer, Profile> fromUser : userProfileHashMap.entrySet()) {
            fromUserSrc = userProfileHashMap.get(fromUser.getKey()).convertToArray();

            nearestUsers.clear();

            for (Map.Entry<Integer, Profile> toUser : userProfileHashMap.entrySet()) {

                // Connect the two arrays storing movie ratings and genome ratings
                toUserSrc = userProfileHashMap.get(toUser.getKey()).convertToArray();

                // Calculate and store similarities in hash-map
                similarity = euclideanDistance(fromUserSrc, toUserSrc);
                nearestUsers.put(toUser.getKey(), similarity);
            }

            // Remove the similarity-score to itself
            nearestUsers.remove(fromUser.getKey());

            // Find new hash-map of only the k nearest neighbors
            nearestUsers = delimit_kNearestNeighbors(nearestUsers, k);

            // Print userID and afterwards the similarities of k users to csv file
            printWriter.print(fromUser.getValue().getID() + ",");

            counter = 0;

            for (Map.Entry<Integer, Double> entry : nearestUsers.entrySet()) {

                printWriter.print(entry.getKey() + "|" + df.format(entry.getValue()));

                if (++counter != k)
                    printWriter.print(",");
            }

            printWriter.println();
        }

        printWriter.close();
    }

    private double euclideanDistance(double[] from, double[] to) {
        int dimensions = from.length - 1;
        double result = 0;

        for (int i = 1; i <= dimensions; i++) {
            result += Math.pow(from[i] - to[i], 2);
        }

        //Calculates the similarity score(between 0, 1) from the euclidean distance
        return 1 / (1 + Math.sqrt(result));
    }
}
