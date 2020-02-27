package Predictors;

public class PredictHybrid implements PredictionMethod {
    private final PredictionMethod collaborate;
    private final PredictionMethod content;
    private final double weight;

    public PredictHybrid(PredictCollaborate collaborate, PredictContent content, double weight) {
        this.collaborate = collaborate;
        this.content = content;
        this.weight = weight;
    }

    public double predictRating(int userID, int movieID) {
        double collaboratePrediction = collaborate.predictRating(userID,movieID);
        double contentPrediction = content.predictRating(userID, movieID);

        // Weigh the the prediction from both collaborate and content
        double prediction = contentPrediction * weight + collaboratePrediction * (1-weight);

        return prediction;
    }
}
