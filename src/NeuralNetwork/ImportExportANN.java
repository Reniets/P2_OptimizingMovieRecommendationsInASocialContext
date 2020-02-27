package NeuralNetwork;

import NeuralNetwork.ANN.ANN;
import NeuralNetwork.ANN.ActivationFunctions.ActivationFunction;
import NeuralNetwork.ANN.ActivationFunctions.ActivationFunctions;
import NeuralNetwork.ANN.Layer;
import NeuralNetwork.ANN.Weight;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;

public class ImportExportANN {

    public ANN importANN(String filePath) {
        ANN network = new ANN();
        int totalLinesRead = 0;
        int expectedLines = 0;

        try {
            // Prepare reading from file
            File file = new File(filePath);
            BufferedReader b = new BufferedReader(new FileReader(file));

            String readLine;

            // While there are data to read.
            while ((readLine = b.readLine()) != null) {

                // If it's the first line, its the header information, and we need to read that in another manner.
                if (totalLinesRead == 0) {
                    expectedLines = readLine.split(",").length;
                    this.setupNetwork(network, expectedLines, readLine);

                    // If the line reading is not the first, it's information about the weights in the layer.
                } else if (totalLinesRead < expectedLines) {
                    this.setWeightsForLayer(network, totalLinesRead, readLine);

                    // If we try to read my lines than originally expected, we will throw an exception, because an error has occurred.
                } else {
                    throw new IllegalArgumentException("The amount of layers read, does not match the expected: " + expectedLines + " vs. " + totalLinesRead);
                }

                // Increment after we read a line
                totalLinesRead++;
            }

            // If an error occurred, catch it and print the error.
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return the imported network
        return network;
    }

    // This method does the initial setup of the network, by adding layers.
    private void setupNetwork(ANN network, int expectedLines, String readLine) {

        for (int i = 0; i < expectedLines; i++) {
            String[] splitLine = readLine.split(",")[i].split("\\|");

            int nodes = Integer.parseInt(splitLine[0]);
            int activationFunctionID = Integer.parseInt(splitLine[1]);

            ActivationFunction AF = ActivationFunctions.getByID(activationFunctionID);
            boolean addLayerSuccess = network.addLayer(nodes, AF);

            if (!addLayerSuccess) {
                throw new IllegalArgumentException("Adding a layer went wrong.");
            }
        }
    }

    // This method sets the weight of the network.
    private void setWeightsForLayer(ANN network, int layerID, String readLine) {
        // Get pointers to current layer and last layer
        Layer lastLayer = network.layers.get(layerID - 1);
        Layer thisLayer = network.layers.get(layerID);

        // For every node in this layer
        int totalNodesInLayer = thisLayer.nodes.size();
        for (int nodeID = 0; nodeID < totalNodesInLayer; nodeID++) {

            // For every weight in this node
            int weightsForThisNode = lastLayer.nodes.size() + 1;
            for (int weightID = 0; weightID < weightsForThisNode; weightID++) {

                // Get pointer to the current weight
                Weight currentWeight = thisLayer.nodes.get(nodeID).weights.get(weightID);

                // Extract the new weight from the read data.
                double newWeightValue = Double.parseDouble(readLine.split(",")[(nodeID * weightsForThisNode) + weightID]);

                // Save the new weight.
                currentWeight.weight = newWeightValue;
            }
        }
    }

    // This method export info about NeuralNetwork.ANN.ANN network, and value of all weights.
    public boolean exportANN(ANN network, String filePath, String fileName){

        try {
            // Get current filepath - add wanted directory and copy to path string

            // Assign filePointer to point at given filepath
            File filePointer = new File(filePath);

            // Check if the filepath already exists
            boolean dirExist = filePointer.exists();

            // If filepath does not exist - Then we create it
            if (!dirExist) {
                // Create new file in specified directory.
                boolean dirCreation = new File(filePath).mkdir();

                if (!dirCreation) {
                    throw new IllegalArgumentException("Creating file export directory went wrong");
                }
            }

            // Prepare printWriter to write in specified file.
            FileWriter fileWriter = new FileWriter(filePath + "/" + fileName, false);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            // For every layer - print total number of nodes and activation function ID
            int totalLayers = network.layers.size();
            for (int layerID = 0; layerID < totalLayers; layerID++) {
                Layer layer = network.layers.get(layerID);

                printWriter.print(layer.nodes.size() + "|" + layer.getAF().getID());
                printWriter.printf((layerID == network.layers.size() - 1) ? "\n" : ",");
            }

            // For every layer that has weights (all except input)
            for (int layerID = 1; layerID < totalLayers; layerID++) {
                int totalNodes = network.layers.get(layerID).nodes.size();

                // For every node in this layer
                for (int nodeID = 0; nodeID < totalNodes; nodeID++) {
                    int totalWeights = network.layers.get(layerID).nodes.get(nodeID).weights.size();

                    // Print every weight in this node
                    for (int weightID = 0; weightID < totalWeights; weightID++) {
                        printWriter.print(network.layers.get(layerID).nodes.get(nodeID).weights.get(weightID).weight);

                        // If it is not the last weight in node - separate values by a comma
                        if (weightID < totalWeights - 1) {
                            printWriter.printf(",");
                        }
                    }

                    // If it is not the last node in layer - separate values by a comma
                    if (nodeID < totalNodes - 1) {
                        printWriter.printf(",");
                    }
                }

                // If it is not the last layer in network - insert newline
                if (layerID < totalLayers - 1) {
                    printWriter.println();
                }
            }

            printWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;

    }
}


