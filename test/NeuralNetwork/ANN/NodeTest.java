package NeuralNetwork.ANN;

import NeuralNetwork.ANN.ActivationFunctions.SigmoidActivation;
import NeuralNetwork.ANN.Node;
import NeuralNetwork.ANN.Weight;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    // Tests if the link between nodes was made by the weight
    @Test
    void nodeWeightLink01() {
        Node oldNode = new Node();
        Node newNode = new Node();

        Random r = new Random();

        Weight weight = new Weight(oldNode, r);

        boolean weightAppended = newNode.appendWeight(weight);

        assertTrue(weightAppended);
        assertEquals(weight.startNode, newNode.weights.get(0).startNode);
    }

    @Test
    void nodeWeightLink02() {
        Node oldNode = new Node();
        Node newNode = new Node();

        Random r = new Random();

        Weight weight = new Weight(oldNode, 0.5, r);

        boolean weightAppended = newNode.appendWeight(weight);

        assertTrue(weightAppended);
        assertEquals(weight.startNode, newNode.weights.get(0).startNode);
    }

    // Tests if the weight actually got appended
    @Test
    void nodeWeightAppend01() {
        Node oldNode = new Node();
        Node newNode = new Node();

        Random r = new Random();

        Weight weight = new Weight(oldNode, r);

        boolean weightAppended = newNode.appendWeight(weight);

        assertTrue(weightAppended);
        assertEquals(weight, newNode.weights.get(0));
    }

    @Test
    void nodeWeightAppend02() {
        Node oldNode = new Node();
        Node newNode = new Node();

        Random r = new Random();

        Weight weight = new Weight(oldNode, 0.5, r);

        boolean weightAppended = newNode.appendWeight(weight);

        assertTrue(weightAppended);
        assertEquals(weight, newNode.weights.get(0));
    }

    // Tests if the weight had the expected weight value
    @Test
    void nodeWeightValue01() {
        Node oldNode = new Node();
        Node newNode = new Node();

        Random r = new Random();

        Weight weight = new Weight(oldNode, r);

        boolean weightAppended = newNode.appendWeight(weight);

        assertTrue(weightAppended);
        assertEquals(weight.weight, newNode.weights.get(0).weight);
    }

    private double sigmoid(double x){
        return 1 / (1 + Math.exp(-x));
    }

    // Tests if a node can update itself
    @Test
    void updateNode01() {
        Node oldNode = new Node();
        oldNode.value = 1;

        Node newNode = new Node();
        newNode.value = 0;

        Random r = new Random();

        Weight weight = new Weight(oldNode, r);
        weight.weight = 0.5;

        boolean weightAppended = newNode.appendWeight(weight);
        boolean nodeUpdated = newNode.updateNode(new SigmoidActivation());

        double x = 1 * 0.5;
        double expected = sigmoid(x);

        assertTrue(weightAppended && nodeUpdated);
        assertEquals(expected, newNode.value);
    }

    @Test
    void updateNode02() {
        Node oldNode = new Node();
        oldNode.value = 1;

        Node newNode = new Node();
        newNode.value = 0;

        Random r = new Random();

        Weight weight = new Weight(oldNode, r);
        weight.weight = -0.5;

        boolean weightAppended = newNode.appendWeight(weight);
        boolean nodeUpdated = newNode.updateNode(new SigmoidActivation());

        double x = 1 * (-0.5);
        double expected = sigmoid(x);

        assertTrue(weightAppended && nodeUpdated);
        assertEquals(expected, newNode.value);
    }

    @Test
    void updateNode03() {
        Node oldNode = new Node();
        oldNode.value = 1;

        Node newNode = new Node();
        newNode.value = 0;

        Random r = new Random();

        Weight weight1 = new Weight(oldNode, r);
        Weight weight2 = new Weight(oldNode, r);

        weight1.weight = 0.5;
        weight2.weight =  -0.5;

        boolean weightsAppended = newNode.appendWeight(weight1);
        weightsAppended = weightsAppended && newNode.appendWeight(weight2);
        boolean nodeUpdated = newNode.updateNode(new SigmoidActivation());

        double x = 1 * (0.5) + 1 * (-0.5);
        double expected = sigmoid(x);

        assertTrue(weightsAppended && nodeUpdated);
        assertEquals(expected, newNode.value);
    }
}