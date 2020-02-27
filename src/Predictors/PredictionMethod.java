package Predictors;

public interface PredictionMethod {
    double predictRating(int userID, int movieID);
}
