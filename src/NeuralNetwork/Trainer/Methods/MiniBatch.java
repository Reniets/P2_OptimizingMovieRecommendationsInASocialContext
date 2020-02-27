package NeuralNetwork.Trainer.Methods;

import NeuralNetwork.ANN.ANN;
import NeuralNetwork.ANN.Layer;
import NeuralNetwork.ANN.Node;
import NeuralNetwork.ANN.Weight;
import NeuralNetwork.Trainer.Costs.CostFunction;
import NeuralNetwork.Trainer.Regularization.Cost.L_Regularization;
import NeuralNetwork.Trainer.TrainingData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MiniBatch extends TrainingMethod {
    private double learningRate;
    private int batchSize;
    private L_Regularization L_Reg = null;
    private double dropout = 0;
    private Random r = new Random();

    // Constructor
    public MiniBatch(CostFunction CF, double learningRate, int batchSize, double dropout, L_Regularization l_Reg) {
        super(CF);
        this.setLearningRate(learningRate);
        this.setBatchSize(batchSize);
        this.setDropout(dropout);
        this.L_Reg = l_Reg;
    }

    public MiniBatch(CostFunction CF, double learningRate, int batchSize, double dropout) {
        this(CF, learningRate, batchSize, dropout, null);
    }

    public MiniBatch(CostFunction CF, double learningRate, int batchSize, L_Regularization l_Reg) {
        this(CF, learningRate, batchSize, 0, l_Reg);
    }

    public MiniBatch(CostFunction CF, double learningRate, int batchSize) {
        this(CF, learningRate, batchSize, 0, null);
    }

    // Setters
    public void setLearningRate(double learningRate) {

        if (learningRate >= 0) {
            this.learningRate = learningRate;
        } else {
            throw new IllegalArgumentException("Learning rate is lower or equal to 0");
        }
    }

    public void setBatchSize(int batchSize) {
        if (batchSize > 0) {
            this.batchSize = batchSize;
        } else {
            throw new IllegalArgumentException("Batch size is lower or equal to 0");
        }
    }

    public void setDropout(double dropout) {
        if (1 >= dropout && dropout >= 0){
            this.dropout = dropout;
        } else {
            throw new IllegalArgumentException("Dropout has to be in the range of [1;0]");
        }
    }

    public void setSeed(int seed){
        this.r = new Random(seed);
    }

    // Training method dependent functions
    @Override
    public void train(ANN network, ArrayList<TrainingData> data) {
        double avgError = 0;
        int counter = 0;

        // Dropout
        if (dropout != 0){
            this.prepareDropout(network);
            this.applyDropout(network);
        }

        int layers = network.layers.size();

        // For every dataPoint
        for (TrainingData currentDataPoint : data) {
            counter++;

            // Feed forward
            ArrayList<Double> prediction = network.evaluateInputs(currentDataPoint.inputs);

            // Calculate error
            avgError += this.calcError(prediction, currentDataPoint);

            // For every layer we backpropagate.
            for (int layerID = layers - 1; layerID > 0; layerID--) {

                // For every node in the layer
                int nodes = network.layers.get(layerID).nodes.size();
                for (int nodeID = 0; nodeID < nodes; nodeID++) {
                    Node currentNode = network.layers.get(layerID).nodes.get(nodeID);

                    if (currentNode.activated){
                        // Calculate the current nodes effect on the TE.
                        this.calculateNodeEffect(network, layerID, nodeID, currentDataPoint, currentNode);

                        // For every weight connected to the node in the layer, calculate the new weight.
                        this.calculateNewWeights(learningRate, currentNode);
                    }
                }
            }

            if (counter % this.batchSize == 0 || counter == data.size()) {
                // Scale and update learned weights

                int scale = ((counter - 1) % this.batchSize) + 1; // Makes sure the last few datapoints are scale probably.

                this.scaleLearnedWeights(network, scale);
                this.updateLearnedWeights(network);

                // Dropout
                if (dropout != 0){
                    this.resetDropout(network);
                    this.applyDropout(network);

                    if (counter == data.size()){
                        this.terminateDropout(network);
                    }
                }
            }
        }

        double regAddon = (this.L_Reg != null) ? this.L_Reg.totalCostAddon(network) : 0;
        this.errorRate = (avgError + regAddon) / data.size();
    }

    private void calculateNodeEffect(ANN network, int layerID, int nodeID, TrainingData currentDataPoint, Node currentNode) {

        if (layerID == network.layers.size() - 1) {
            currentNode.effect = CF.costPrime(currentDataPoint.outputs[nodeID], currentNode.value);

        } else {
            currentNode.effect = 0;

            // For every node in the next layer
            for (Node nextNode : network.layers.get(layerID + 1).nodes) {

                if (nextNode.activated){
                    // Find the connecting weight between currentNode and NextNode
                    int ID = nodeID + 1;

                    // accumulate the effect
                    currentNode.effect += nextNode.effect * nextNode.weights.get(ID).weight;
                }

                //FIXME: Optimering (Linjen forneden er blevet optimeret væk)
                //while (nextNode.weights.get(++ID).startNode != currentNode); // Find a better solution, maybe change the way nodes and weights work
            }

        }
        currentNode.effect *= network.layers.get(layerID).getAF().activationPrime(currentNode.value);
    }

    private void calculateNewWeights(double learningRate, Node currentNode) {
        boolean bias = true; 

        double regAddon, d, weightChange;

        int totalWeights = currentNode.weights.size();
        // For every weight connected to this node.
        for (int i = 0; i < totalWeights; i++) {
            Weight currentWeight = currentNode.weights.get(i);

            // Calculate L_Regulization addition
            if (this.L_Reg != null){
                if (bias){
                    regAddon = this.L_Reg.biasDerivativeAddon(currentWeight);
                } else {
                    regAddon = this.L_Reg.weightDerivativeAddon(currentWeight);
                }
            } else {
                regAddon = 0;
            }

            // Calculate the derivative of the weight
            d = (currentNode.effect * currentWeight.startNode.value) - regAddon;

            // Calculate how much to change the weight
            weightChange = currentWeight.weight - (learningRate * d);

            // Update the updateValue accordingly
            currentWeight.updateValue += weightChange;

            // Set bias to false.
            bias = false;
        }
    }

    private void applyDropout(ANN network){

        int totalLayers = network.layers.size();

        for (int i = 1; i < totalLayers - 1; i++){ //Skip first and last layer.
            Layer layer = network.layers.get(i);

            int totalNodes = layer.nodes.size();
            int droppedNodes = (int) (totalNodes * dropout);
            Set<Integer> droppedNodeID = new HashSet<>(droppedNodes);

            while (droppedNodeID.size() != droppedNodes){
                droppedNodeID.add(r.nextInt(totalNodes));
            }

            for (Object ID : droppedNodeID){
                layer.nodes.get((Integer) ID).activated = false;
            }
        }
    }

    private void resetDropout(ANN network){
        for (Layer layer : network.layers){

            for (Node node : layer.nodes){
                node.activated = true;
            }
        }
    }

    private void terminateDropout(ANN network){
        int totalLayers = network.layers.size();

        for (int i = 1; i < totalLayers; i++){
            Layer layer = network.layers.get(i);

            for (Node node : layer.nodes){
                for (Weight weight : node.weights){
                    weight.weight *= (1 - dropout);
                }
            }
        }
    }

    private void prepareDropout(ANN network){
        int totalLayers = network.layers.size();

        for (int i = 1; i < totalLayers; i++){
            Layer layer = network.layers.get(i);

            for (Node node : layer.nodes){
                for (Weight weight : node.weights){
                    weight.weight *= (1 + dropout);
                }
            }
        }
    }

    private void scaleLearnedWeights(ANN network, int size) {

        if (this.batchSize != 1) { // Optimization, since division by 1, has zero effect.
            // For every layer
            for (Layer currentLayer : network.layers) {

                // For every node in the layer
                for (Node currentNode : currentLayer.nodes) {

                    if (currentNode.activated){

                        // For every weight in the node
                        for (Weight currentWeight : currentNode.weights) {

                            // Scale it (Average)
                            currentWeight.updateValue /= size;
                        }
                    }
                }
            }
        }
    }
}
