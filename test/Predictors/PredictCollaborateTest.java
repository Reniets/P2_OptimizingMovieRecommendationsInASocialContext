package Predictors;

import KNN.PearsonCorrelation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PredictCollaborateTest {
    private PearsonCorrelation pearsonCorrelation;
    private PredictCollaborate predictCollaborate;

    @BeforeEach
    void exportAvgRatings() {
        pearsonCorrelation = new PearsonCorrelation(5);

        try {
            pearsonCorrelation.exportCorrelations(3);
            pearsonCorrelation.exportAvgOverallRatings();
        } catch (IOException e) {
            e.printStackTrace();
        }

        predictCollaborate = new PredictCollaborate(5, 3);
    }

    // Test that if a users has already rated the movie we seek a rating from then return the current rating
    @Test
    void predictRating01() {
        assertEquals(4, predictCollaborate.predictRating(1, 2398));
    }

    // Test that a prediction on a movie that no other users has seen returns the average predictions
    @Test
    void predictRating02() {
        assertEquals(pearsonCorrelation.averageOverallRating(1), predictCollaborate.predictRating(1, 1000));
    }

    // Test that two users with a correlation of 1 predicts the correct rating
    @Test
    void predictRating03() {
        assertEquals(5.0, predictCollaborate.predictRating(5, 109));
    }
}
