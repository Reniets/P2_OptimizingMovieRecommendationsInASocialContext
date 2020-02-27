package NeuralNetwork.Trainer.Regularization.Cost;

import NeuralNetwork.ANN.ANN;
import NeuralNetwork.ANN.Layer;
import NeuralNetwork.ANN.Node;
import NeuralNetwork.ANN.Weight;

public class L2_Regularization implements L_Regularization{
    private double lambda;

    public L2_Regularization(double lambda) {
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
                    weightSum += Math.pow(weight.weight, 2);
                }
            }
        }

        return (this.lambda / 2) * weightSum;
    }

    @Override
    public double weightDerivativeAddon(Weight weight) {
        return this.lambda * weight.weight;
    }

    @Override
    public double biasDerivativeAddon(Weight weight) {
        return 0;
    }
}
