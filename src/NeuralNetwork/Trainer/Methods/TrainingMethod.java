package NeuralNetwork.Trainer.Methods;

import NeuralNetwork.ANN.ANN;
import NeuralNetwork.ANN.Node;
import NeuralNetwork.ANN.Weight;
import NeuralNetwork.Trainer.Costs.CostFunction;
import NeuralNetwork.Trainer.TrainingData;

import java.util.ArrayList;

public abstract class TrainingMethod {
    double errorRate = 0;
    final CostFunction CF;

    public TrainingMethod(CostFunction CF) {
        this.CF = CF;
    }

    public double getErrorRate() {
        return errorRate;
    }

    // Calculates the error from one datapoint, and returns it.
    final double calcError(ArrayList<Double> prediction, TrainingData goal){
        double error = 0;

        int totalOutputs = prediction.size();
        for (int i = 0; i < totalOutputs; i++){
            error += CF.cost(goal.outputs[i], prediction.get(i));
        }

        return error;
    }

    void updateLearnedWeights(ANN network){
        // For every layer
        int layers = network.layers.size();
        for (int layerID = layers - 1; layerID > 0; layerID--){

            // For every node in the layer
            for (Node currentNode : network.layers.get(layerID).nodes){

                if (currentNode.activated){

                    // For every weight in the node
                    for (Weight currentWeight : currentNode.weights){

                        // Update it
                        currentWeight.updateWeight();

                        // Reset it
                        currentWeight.updateValue = 0;
                    }
                }
            }
        }
    }

    public abstract void train(ANN network, ArrayList<TrainingData> data);
}
