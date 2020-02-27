package NeuralNetwork.Trainer.Regularization.Cost;

import NeuralNetwork.ANN.ANN;
import NeuralNetwork.ANN.Weight;

public interface L_Regularization {

    double getlambda();
    void setlambda(double lambda);

    double totalCostAddon(ANN network);

    double weightDerivativeAddon(Weight weight);

    double biasDerivativeAddon(Weight weight);
}
