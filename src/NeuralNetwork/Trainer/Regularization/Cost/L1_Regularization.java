package NeuralNetwork.Trainer.Regularization.Cost;

import NeuralNetwork.ANN.ANN;
import NeuralNetwork.ANN.Layer;
import NeuralNetwork.ANN.Node;
import NeuralNetwork.ANN.Weight;

public class L1_Regularization implements L_Regularization {
    private double lambda;

    public L1_Regularization(double lambda) {
        setlambda(lambda);
    }

    @Override
    public double getlambda() {
        return this.lambda;
    }

    @Override
    public void setlambda(double lambda) {
        if (lambda > 0){
            this.lambda = lambda;
        } else {
            throw new IllegalArgumentException("Lamda value is lower than 0");
        }
    }

    @Override
    public double totalCostAddon(ANN network) {
        double weightSum = 0;

        for (Layer layer : network.layers){
            for (Node node : layer.nodes){
                for (Weight weight : node.weights){
                    weightSum += Math.abs(weight.weight);
                }
            }
        }

        return this.lambda * weightSum;
    }

    @Override
    public double weightDerivativeAddon(Weight weight) {
        double sign;

        if (weight.weight == 0){
            sign = 0;
        } else if (weight.weight > 0){
            sign = 1;
        } else if (weight.weight < 0){
            sign = -1;
        } else {
            throw new IllegalArgumentException("The weight value is not a number? Probably null...");
        }

        return this.lambda * sign;
    }

    @Override
    public double biasDerivativeAddon(Weight weight) {
        return 0;
    }
}
