package NeuralNetwork.ANN;

import NeuralNetwork.ANN.ActivationFunctions.ActivationFunction;

import java.util.ArrayList;
import java.util.Random;

public class Layer {
    private ActivationFunction AF;
    public ArrayList<Node> nodes;

    public Layer(int totalNodes, ActivationFunction AF) throws IllegalArgumentException {
        // A layer needs at least 1 node to work.
        if (totalNodes <= 0){
            throw new IllegalArgumentException("A layer cannot contain 0 or less nodes");
        }

        this.AF = AF;

        // Makes node list, and add nodes to the list.
        this.nodes = new ArrayList<>(totalNodes);

        for (int i = 0; i < totalNodes; i++){
            this.nodes.add(new Node());
        }
    }

    public ActivationFunction getAF() {
        return AF;
    }

    // Method to update all nodes in the layer (Feed forward)
    public boolean updateLayer(){
        boolean success = false;

        // For each node, update it.
        int totalNodes = nodes.size();
        for (int i = 0; i < totalNodes; i++){
            Node currentNode = nodes.get(i);

            if (currentNode.activated){
                success = currentNode.updateNode(this.AF);

                if (!success){
                    throw new RuntimeException("A node could not be updated");
                }
            }


        }

        return success;
    }

    // Method to connect one layer to another.
    public boolean connectTo(Layer nextLayer, Random r){
        boolean success = false;

        outerLoop:
        // For every node in this layer
        for (Node thisNode : this.nodes){

            // To every node in the next layer
            for (Node nextNode : nextLayer.nodes){

                // Connect them with a weight.
                success = nextNode.appendWeight(new Weight(thisNode, r));

                if (!success){
                    break outerLoop;
                }
            }
        }

        return success;
    }

    // Method to return all the values of the current layer.
    public ArrayList<Double> nodeValues(){
        ArrayList<Double> values = new ArrayList<>(nodes.size());

        for (Node node : nodes){
            values.add(node.value);
        }

        return values;
    }
}
