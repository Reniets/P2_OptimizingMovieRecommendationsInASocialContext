package NeuralNetwork.ANN;

import NeuralNetwork.ANN.ActivationFunctions.ActivationFunction;

import java.util.ArrayList;
import java.util.Random;

public class ANN {
    public final ArrayList<Layer> layers = new ArrayList<>();
    public final Node bias = new Node();
    private Random r = new Random();

    // Constructor for a fully size customizable network
    public ANN() {
        this.bias.value = 1;
    }

    // Constructor for a homogeneous network
    public ANN(int inputs, int outputs, int hiddenLayers, int hiddenNeurons, ActivationFunction AF) {
        this(inputs, outputs, hiddenLayers, hiddenNeurons, AF, null);
    }

    public ANN(int inputs, int outputs, int hiddenLayers, int hiddenNeurons, ActivationFunction AF, Integer seed){
        this();

        if (seed != null){
            r = new Random(seed);
        }

        // Add layers
        this.addLayer(inputs, AF);

        for (int i = 0; i < hiddenLayers; i++) {
            this.addLayer(hiddenNeurons, AF);
        }

        this.addLayer(outputs, AF);
    }

    // Method to add a layer to an already existing network
    public boolean addLayer(int nodes, ActivationFunction AF) {
        boolean addSuccess, connectSucces = true;
        int totalLayers;
        Layer currentLayer, pastLayer;

        // Add layer
        addSuccess = this.layers.add(new Layer(nodes, AF));

        // Connect layers
        totalLayers = this.layers.size();

        if (totalLayers > 1) {   // We need atleast 2 layers to connect them.
            currentLayer = this.layers.get(totalLayers - 1);
            pastLayer = this.layers.get(totalLayers - 2);

            connectBiasTo(currentLayer);
            connectSucces = pastLayer.connectTo(currentLayer, r);
        }

        // Returns true if the layer was connected
        return addSuccess && connectSucces;
    }

    private void connectBiasTo(Layer layer){
        for (Node currentNode : layer.nodes){
            currentNode.appendWeight(new Weight(this.bias, r));
        }
    }

    // Method to evaluate inputs and return the output / predictions
    public ArrayList<Double> evaluateInputs(double[] inputs) {

        // In case the input size does not fit the input layer size.
        if (inputs.length != layers.get(0).nodes.size()) {
            throw new IllegalArgumentException("The input size, is not equal to the input layer size: " + inputs.length + " vs. " + layers.get(0).nodes.size());
        }

        // Set inputs
        int totalInputNodes = layers.get(0).nodes.size();
        for (int i = 0; i < totalInputNodes; i++) {
            layers.get(0).nodes.get(i).value = inputs[i];
        }

        // Update layers
        int totalLayers = layers.size();
        for (int i = 1; i < totalLayers; i++) {
            layers.get(i).updateLayer();
        }

        // Output
        return layers.get(totalLayers - 1).nodeValues();
    }

}
