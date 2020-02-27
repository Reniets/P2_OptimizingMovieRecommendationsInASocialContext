package NeuralNetwork.ANN;

import NeuralNetwork.ANN.ActivationFunctions.SigmoidActivation;
import NeuralNetwork.ANN.Layer;
import NeuralNetwork.ANN.Node;
import NeuralNetwork.ANN.Weight;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class LayerTest {

    // Tests if a layer can update itself
    @Test
    void updateLayer01() {
        Node someNode = new Node();
        someNode.value = 1;
        Random r = new Random();
        Weight someWeight = new Weight(someNode,0, r);

        Layer layer = new Layer(1, new SigmoidActivation());
        boolean weightAppended = layer.nodes.get(0).appendWeight(someWeight);
        boolean layerUpdated = layer.updateLayer();

        assertTrue(weightAppended && layerUpdated);
        assertEquals(0.5, layer.nodes.get(0).value);
    }

    @Test
    void updateLayer02() {
        Node someNode = new Node();
        someNode.value = 1;
        Random r = new Random();
        Weight someWeight = new Weight(someNode, r);
        someWeight.weight = 0.5;

        Layer layer = new Layer(1, new SigmoidActivation());
        boolean weightAppended = layer.nodes.get(0).appendWeight(someWeight);
        boolean layerUpdated = layer.updateLayer();

        double x = 1 * 0.5;
        double expected = 1 / (1 + Math.exp(-x));

        assertTrue(weightAppended && layerUpdated);
        assertEquals(expected, layer.nodes.get(0).value);
    }

    @Test
    void updateLayer03() {
        // Update a layer with 0 nodes.
        Layer layer = new Layer(1, new SigmoidActivation()); // totalNodes: 0 Throws exception
        layer.nodes.remove(0);

        boolean layerUpdated = layer.updateLayer();

        assertFalse(layerUpdated);
    }

    @Test
    void updateLayer04() {

        Assertions.assertThrows(RuntimeException.class, () -> {
            Layer layer = new Layer(1, new SigmoidActivation());

            /* The nodes in the layer is not connected to any nodes, and therefor cannot update itself. */
            layer.updateLayer();
        });
    }

    // Tests if a layer can connect to another
    @Test
    void connectTo01() {
        Layer layer1 = new Layer(1, new SigmoidActivation());
        Layer layer2 = new Layer(1, new SigmoidActivation());
        Random r = new Random();
        boolean layerConnected = layer1.connectTo(layer2, r);

        assertTrue(layerConnected);
        assertLayerConnectedTo(layer1, layer2);
    }

    @Test
    void connectTo02() {
        Layer layer1 = new Layer(2, new SigmoidActivation());
        Layer layer2 = new Layer(1, new SigmoidActivation());
        Random r = new Random();
        boolean layerConnected = layer1.connectTo(layer2, r);

        assertTrue(layerConnected);
        assertLayerConnectedTo(layer1, layer2);
    }

    @Test
    void connectTo03() {
        Layer layer1 = new Layer(1, new SigmoidActivation());
        Layer layer2 = new Layer(2, new SigmoidActivation());
        Random r = new Random();
        boolean layerConnected = layer1.connectTo(layer2, r);

        assertTrue(layerConnected);
        assertLayerConnectedTo(layer1, layer2);
    }

    @Test
    void connectTo04() {
        // 0 nodes in the last layer
        Layer layer1 = new Layer(1, new SigmoidActivation());
        Layer layer2 = new Layer(1, new SigmoidActivation()); // totalNodes: 0 throws exception
        layer2.nodes.remove(0);
        Random r = new Random();
        boolean layerConnected = layer1.connectTo(layer2, r);

        assertFalse(layerConnected);
    }

    @Test
    void connectTo05() {
        // 0 nodes in the first layer
        Layer layer1 = new Layer(1, new SigmoidActivation()); // totalNodes: 0 throws exception
        layer1.nodes.remove(0);

        Layer layer2 = new Layer(1, new SigmoidActivation());
        Random r = new Random();
        boolean layerConnected = layer1.connectTo(layer2, r);

        assertFalse(layerConnected);
    }

    @Test
    void connectTo06() {
        // 0 Nodes in both layers
        Layer layer1 = new Layer(1, new SigmoidActivation()); // totalNodes: 0 throws exception
        Layer layer2 = new Layer(1, new SigmoidActivation()); // totalNodes: 0 throws exception
        layer1.nodes.remove(0);
        layer2.nodes.remove(0);

        Random r = new Random();
        boolean layerConnected = layer1.connectTo(layer2, r);

        assertFalse(layerConnected);
    }

    private void assertLayerConnectedTo(Layer layer1, Layer layer2) {
        for (int layer2NodeID = 0; layer2NodeID < layer2.nodes.size(); layer2NodeID++){
            Node layer2Node = layer2.nodes.get(layer2NodeID);

            for (int layer1NodeID = 0; layer1NodeID < layer1.nodes.size(); layer1NodeID++) {
                Node layer1Node = layer1.nodes.get(layer1NodeID);
                Weight currentWeight = layer2Node.weights.get(layer1NodeID);


                assertEquals(currentWeight.startNode, layer1Node);
            }
        }
    }

    // Tests if a layer can return the values of it's nodes
    @Test
    void nodeValues01() {
        Layer layer = new Layer(1, new SigmoidActivation());
        layer.nodes.get(0).value = 0.5;

        ArrayList<Double> expected = new ArrayList<>(1);
        expected.add(0.5);

        assertArrayEquals(expected.toArray(), layer.nodeValues().toArray());
    }

    @Test
    void nodeValues02() {
        Layer layer = new Layer(2, new SigmoidActivation());
        layer.nodes.get(0).value = 0.5;
        layer.nodes.get(1).value = 0.25;

        ArrayList<Double> expected = new ArrayList<>(2);
        expected.add(0.5);
        expected.add(0.25);

        assertArrayEquals(expected.toArray(), layer.nodeValues().toArray());
    }

    @Test
    void nodeValues03() {
        Layer layer = new Layer(1, new SigmoidActivation());
        layer.nodes.remove(0);

        // 0 Nodes in the layer
        ArrayList<Double> expected = new ArrayList<>();

        assertArrayEquals(expected.toArray(), layer.nodeValues().toArray());
    }

}