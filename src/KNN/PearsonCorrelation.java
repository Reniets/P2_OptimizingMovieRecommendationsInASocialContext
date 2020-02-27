package KNN;

import Other.DataImporter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

public class PearsonCorrelation extends HandleNeighbors {
    private final HashMap<Integer, HashMap<Integer, Double>> seenBy;
    private final int fileNumber;

    public PearsonCorrelation(int fileNumber) {
        this.seenBy = DataImporter.importSeenBy(fileNumber);
        this.fileNumber = fileNumber;
    }

    public HashMap<Integer, HashMap<Integer, Double>> getSeenBy() {
        return seenBy;
    }

    public void exportCorrelations(int k) throws IOException {
        HashMap<Integer, HashMap<Integer, Double>> correlationAllUsers = new HashMap<>();
        Double correlation;

        for (Map.Entry<Integer, HashMap<Integer, Double>> fromUser : this.seenBy.entrySet()) {
            correlationAllUsers.put(fromUser.getKey(), new HashMap<>());

            for (Map.Entry<Integer, HashMap<Integer, Double>> toUser : this.seenBy.entrySet()) {

                if(fromUser.getKey() < toUser.getKey()) {
                    correlation = calcCorrelation(fromUser.getKey(), toUser.getKey());

                    correlationAllUsers.get(fromUser.getKey()).put(toUser.getKey(), correlation);
                }
            }
        }

        printCorrelationsToFile(correlationAllUsers, k);
    }

    public void printCorrelationsToFile(HashMap<Integer, HashMap<Integer, Double>> correlationAllUsers, int k) throws IOException {
        LinkedHashMap<Integer, Double> tempCorrelationUser = new LinkedHashMap<>();
        int counterUsers;

        DecimalFormat df = new DecimalFormat("0.000", DecimalFormatSymbols.getInstance(Locale.US));

        // Get filepath to where correlations should be exported to
        String filePath = "data/sep/" + this.fileNumber + "/pearsonCorrelations/pearsonCorrelations_" + k + "_" + this.fileNumber + ".csv";

        // Prepare printWriter to write in specified file.
        FileWriter fileWriter = new FileWriter(filePath, false);
        PrintWriter printWriter = new PrintWriter(fileWriter);

        // Looping through seenBy to print everything to file
        for (Map.Entry<Integer, HashMap<Integer, Double>> fromUser : this.seenBy.entrySet()) {
            tempCorrelationUser.clear();

            for (Map.Entry<Integer, HashMap<Integer, Double>> toUser : this.seenBy.entrySet()) {
                if (fromUser.getKey() > toUser.getKey())
                    tempCorrelationUser.put(toUser.getKey(), correlationAllUsers.get(toUser.getKey()).get(fromUser.getKey()));
            }

            tempCorrelationUser.putAll(correlationAllUsers.get(fromUser.getKey()));

            // Find only the k nearest neighbors
            tempCorrelationUser = delimit_kNearestNeighbors(tempCorrelationUser, k);

            // Print userID and afterwards the correlations of k users to csv file
            printWriter.print(fromUser.getKey() + ",");

            counterUsers = 0;

            for (Map.Entry<Integer, Double> entry : tempCorrelationUser.entrySet()) {

                printWriter.print(entry.getKey() + "|" + df.format(entry.getValue()));

                if (++counterUsers != k)
                    printWriter.print(",");
            }

            printWriter.println();
        }

        printWriter.close();
    }

    double calcCorrelation(int userID_A, int userID_B) {
        double meanCenteredProduct = 0, lengthUser_A = 0, lengthUser_B = 0, movieRatingA, movieRatingB;

        double avgRatingA = averageOverallRating(userID_A);
        double avgRatingB = averageOverallRating(userID_B);

        HashMap<Integer, Double> userA = this.seenBy.get(userID_A);
        HashMap<Integer, Double> userB = this.seenBy.get(userID_B);

        // Make set of all movies user A has seen
        Set<Integer> userMoviesA = new HashSet<>(this.seenBy.get(userID_A).keySet());

        // Make set of all movies user b has seen
        Set<Integer> userMoviesB = new HashSet<>(this.seenBy.get(userID_B).keySet());

        // Find intersection between the two sets to find the movies they have in common
        userMoviesB.retainAll(userMoviesA);

        // Return correlation 0 if user A and B have not seen more than 5 of the same movies
        if (userMoviesB.size() < 7) {
            return 0;
        }

        // Loop through all movies user A and B has in common
        // Calculate their correlation
        for (Integer movieEntry : userMoviesB) {
            movieRatingA = userA.get(movieEntry);
            movieRatingB = userB.get(movieEntry);

            meanCenteredProduct += (movieRatingA - avgRatingA) * (movieRatingB - avgRatingB);
            lengthUser_A += (movieRatingA - avgRatingA) * (movieRatingA - avgRatingA);
            lengthUser_B += (movieRatingB - avgRatingB) * (movieRatingB - avgRatingB);
        }

        lengthUser_A = Math.sqrt(lengthUser_A);
        lengthUser_B = Math.sqrt(lengthUser_B);

        if (lengthUser_A * lengthUser_B == 0)
            return 0;

        return meanCenteredProduct / (lengthUser_A * lengthUser_B);
    }

    public double averageOverallRating(int userID_A) {
        double total = 0;

        for (Map.Entry<Integer, Double> entry : this.seenBy.get(userID_A).entrySet()) {
            total += entry.getValue();
        }

        return total / (double) this.seenBy.get(userID_A).size();
    }

    public void exportAvgOverallRatings() throws IOException {
        double tempAvgOverallRating;
        DecimalFormat df = new DecimalFormat("0.000", DecimalFormatSymbols.getInstance(Locale.US));

        String filePath = "data/sep/" + this.fileNumber + "/avgOverallRatings_" + this.fileNumber + ".csv";

        // Prepare printWriter to write in specified file.
        FileWriter fileWriter = new FileWriter(filePath, false);
        PrintWriter printWriter = new PrintWriter(fileWriter);

        for (Map.Entry<Integer, HashMap<Integer, Double>> fromUser : this.seenBy.entrySet()) {
            tempAvgOverallRating = averageOverallRating(fromUser.getKey());

            // Print userID and afterwards the correlations of k users to csv file
            printWriter.println(fromUser.getKey() + "|" + df.format(tempAvgOverallRating));
        }
        printWriter.close();

    }
}
