package NeuralNetwork.ANN;

import NeuralNetwork.ANN.ActivationFunctions.ActivationFunction;

import java.util.ArrayList;

public class Node {
    public double value;
    public final ArrayList<Weight> weights = new ArrayList<>();
    public boolean activated = true;

    // Used for training, to store this nodes effect on all future layers and output.
    // Maybe another method for this, should be found. Most likely in the NeuralNetwork.Trainer.Trainer class.
    public double effect;

    // Appends a weight to a node.
    public boolean appendWeight(Weight w){
        return this.weights.add(w);
    }

    // Method to update this node (Feed forward)
    public boolean updateNode(ActivationFunction AF){
        double sumValues = 0;
        int totalWeights = this.weights.size();

        // For every weight going into this node, update the sum input value for this node
        for (int i = 0; i < totalWeights; i++){
            Weight currentWeight = this.weights.get(i);

            sumValues += currentWeight.startNode.value * currentWeight.weight;
        }

        this.value = 0;

        // Then apply the activation function
        this.value = AF.activation(sumValues);

        boolean anyWeights = totalWeights != 0;

        // Returns false if the result is Nan, infinite or if no weights exists.

        boolean finite = Double.isFinite(this.value);

        return anyWeights && finite;
    }
}
