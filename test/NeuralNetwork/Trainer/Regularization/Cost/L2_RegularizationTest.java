package NeuralNetwork.Trainer.Regularization.Cost;

import NeuralNetwork.ANN.ANN;
import NeuralNetwork.ANN.Node;
import NeuralNetwork.ANN.Weight;
import NeuralNetwork.ImportExportANN;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class L2_RegularizationTest {

    @Test
    void totalCostAddon() {
        ImportExportANN IEANN = new ImportExportANN();
        ANN network = IEANN.importANN("Tests/Predictors/testData/PredictContent/simpleANN.csv");
        L_Regularization L2 = new L2_Regularization(1);

        double expected = (104 * 2 + 2) * 0.5;

        assertEquals(expected, L2.totalCostAddon(network));
    }

    @Test
    void weightDerivativeAddon() {
        Weight weight = new Weight(new Node(), new Random());
        weight.weight = 1;
        L_Regularization L2 = new L2_Regularization(1);

        assertEquals(1, L2.weightDerivativeAddon(weight));
    }

    @Test
    void biasDerivativeAddon() {
        Weight weight = new Weight(new Node(), new Random());
        L_Regularization L2 = new L2_Regularization(1);

        assertEquals(0, L2.biasDerivativeAddon(weight));
    }
}