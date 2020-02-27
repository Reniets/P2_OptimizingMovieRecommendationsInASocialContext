package Predictors;

import Other.DataImporter;

import java.util.HashMap;

public class PredictCollaborate implements PredictionMethod {
    private final HashMap<Integer, HashMap<Integer, Double>> seenBy;
    private final HashMap<Integer, HashMap<Integer, Double>> correlations;
    private final HashMap<Integer, Double> avgOverallRatings;

    public PredictCollaborate(int fileNumber, int k) {
        this.seenBy = DataImporter.importSeenBy(fileNumber);
        this.correlations = DataImporter.importCorrelations(fileNumber, k);
        this.avgOverallRatings = DataImporter.importAvgOverallRatings(fileNumber);
    }

    public double predictRating(int userID, int movieID) {
        double result = 0, weightSum = 0, rating, correlation, avgOverallRating;
        int numberOfUsersSeenMovie = 0;

        if(this.seenBy.get(userID).containsKey(movieID)) {
            return this.seenBy.get(userID).get(movieID);
        }

        HashMap<Integer, Double> userCorrelations = correlations.get(userID);

        for (Integer neighborID : userCorrelations.keySet()) {
            HashMap<Integer, Double> neighborRatings = seenBy.get(neighborID);

            // Make sure that the user has rated this movie and a correlation between the users is stored
            if (neighborRatings.containsKey(movieID)) {
                correlation = userCorrelations.get(neighborID);
                rating = neighborRatings.get(movieID);
                avgOverallRating = avgOverallRatings.get(neighborID);

                result += ((rating - avgOverallRating) * correlation);
                weightSum += correlation;

                numberOfUsersSeenMovie++;
            }
        }

        if (numberOfUsersSeenMovie < 3 || (0 < weightSum && weightSum < 0.001) || result == 0)
            return avgOverallRatings.get(userID);

        return avgOverallRatings.get(userID) + result / weightSum;
    }
}
