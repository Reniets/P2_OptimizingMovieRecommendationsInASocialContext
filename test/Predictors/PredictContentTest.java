package Predictors;

import NeuralNetwork.ANN.ANN;
import NeuralNetwork.ANN.ActivationFunctions.ActivationFunction;
import NeuralNetwork.ANN.ActivationFunctions.SigmoidActivation;
import NeuralNetwork.ImportExportANN;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class PredictContentTest {

    @Test
    void prediction01(){
        ImportExportANN IEANN = new ImportExportANN();

        ANN network = IEANN.importANN("Tests/Predictors/testData/PredictContent/simpleANN.csv");

        PredictContent content = new PredictContent(0, network);

        ActivationFunction sigmoid =  new SigmoidActivation();

        ArrayList<Double> input = new ArrayList<>(104);

        double[] userVector = content.getUserProfileList().get(1).convertToArray();
        double[] movieVector = content.getMovieProfileList().get(1).convertToArray();

        for (int i = 1; i < userVector.length; i++) {
            input.add(userVector[i]);
        }

        for (int i = 1; i < movieVector.length; i++) {
            input.add(movieVector[i]);
        }

        double sumOfInputs = 0;

        for (Double value : input) {
            sumOfInputs += value;
        }

        double expected = sigmoid.activation(sigmoid.activation(sumOfInputs) * 2) * 5;

        double prediction = content.predictRating(1, 1);

        Assertions.assertEquals(expected, prediction, 0.001);
    }

}
