package Predictors;

import NeuralNetwork.ANN.ANN;
import NeuralNetwork.ANN.Layer;
import Other.DataImporter;
import Profiles.Profile;

import java.util.ArrayList;
import java.util.HashMap;

public class PredictContent implements PredictionMethod {
    private ANN network;
    private final HashMap<Integer, Profile> movieProfileList;
    private final HashMap<Integer, Profile> userProfileList;
    private final int inputSize;


    public PredictContent(int fileNumber, ANN network) {
        this.movieProfileList = DataImporter.importMovieProfileList(fileNumber);
        this.userProfileList = DataImporter.importUserProfileList(fileNumber);

        this.inputSize = (userProfileList.get(1).convertToArray().length - 1) * 2;

        this.setNetwork(network);
    }

    public HashMap<Integer, Profile> getMovieProfileList() {
        return movieProfileList;
    }

    public HashMap<Integer, Profile> getUserProfileList() {
        return userProfileList;
    }

    private void setNetwork(ANN network) {
        ArrayList<Layer> layers = network.layers;
        int inputLayerSize = layers.get(0).nodes.size();
        int outputLayerSize = layers.get(layers.size() - 1).nodes.size();

        if (inputLayerSize == this.inputSize && outputLayerSize == 1) {
            this.network = network;
        } else {
            throw new IllegalArgumentException("The network does not have the right configurations Input: " + inputLayerSize + " Output: " + outputLayerSize);
        }
    }

    public double predictRating(int userID, int movieID) {
        double[] input = new double[inputSize];

        double[] userSrc = userProfileList.get(userID).convertToArray();
        double[] movieSrc = movieProfileList.get(movieID).convertToArray();
        System.arraycopy(userSrc, 1, input, 0, userSrc.length - 1);
        System.arraycopy(movieSrc, 1, input, userSrc.length - 1, movieSrc.length - 1);

        // Antaget at vi kun har 1 output.
        double prediction = this.network.evaluateInputs(input).get(0);

        return prediction * 5;
    }
}
