package NeuralNetwork.ANN;

import NeuralNetwork.ANN.ANN;
import NeuralNetwork.ANN.ActivationFunctions.SigmoidActivation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ANNTest {

    // Tests if a network can add layers to itself
    @Test
    void ANNConstructor01() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new ANN(0,1,1,1, new SigmoidActivation());
        });
    }

    @Test
    void ANNConstructor02() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new ANN(1,0,1,1, new SigmoidActivation());
        });
    }

    @Test
    void ANNConstructor03() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new ANN(1,0,1,0, new SigmoidActivation());
        });
    }

    // Tests if a network can add layers to itself
    @Test
    void addLayer01() {
        ANN network = new ANN();

        boolean layerAdded = network.addLayer(1, new SigmoidActivation());

        assertTrue(layerAdded);
    }

    @Test
    void addLayer02() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ANN network = new ANN();
            network.addLayer(0, new SigmoidActivation());
        });
    }

    // Tests if a network can evaluate inputs to outputs correctly
    @Test
    void evaluateInputs01() {
        ANN network = new ANN(1,1,0,0, new SigmoidActivation());
        network.layers.get(1).nodes.get(0).weights.get(0).weight = 1;
        double[] inputs = {0};

        ArrayList<Double> expected = new ArrayList<>(1);
        expected.add(sigmoid(1));

        assertArrayEquals(expected.toArray(), network.evaluateInputs(inputs).toArray());
    }

    @Test
    void evaluateInputs02() {
        ANN network = new ANN(1,1,1,1, new SigmoidActivation());
        network.layers.get(1).nodes.get(0).weights.get(0).weight = 1;
        network.layers.get(2).nodes.get(0).weights.get(0).weight = 2;
        double[] inputs = {0};
        double x = sigmoid(1) * network.layers.get(2).nodes.get(0).weights.get(1).weight;

        ArrayList<Double> expected = new ArrayList<>(1);
        expected.add(sigmoid(x+2));

        assertArrayEquals(expected.toArray(), network.evaluateInputs(inputs).toArray());
    }

    @Test
    void evaluateInputs03() {
        ANN network = new ANN(2,1,1,2, new SigmoidActivation());
        network.layers.get(1).nodes.get(0).weights.get(0).weight = 1;
        network.layers.get(1).nodes.get(1).weights.get(0).weight = 2;
        network.layers.get(2).nodes.get(0).weights.get(0).weight = 3;
        double[] inputs = {0,0};
        double x = sigmoid(1) * network.layers.get(2).nodes.get(0).weights.get(1).weight + sigmoid(2) * network.layers.get(2).nodes.get(0).weights.get(2).weight;

        double[] expected = {sigmoid(x + 3)};

        ArrayList<Double> prediction = (network.evaluateInputs(inputs));

        assertEquals(expected.length, prediction.size());

        for (int i = 0; i < prediction.size(); i++) {
            assertEquals(expected[i], prediction.get(i), 0.0001);
        }

    }

    @Test
    void evaluateInputs04() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ANN network = new ANN(2,1,1,2, new SigmoidActivation());
            double[] inputs = {0};

            network.evaluateInputs(inputs);
        });
    }

    private double sigmoid(double x){
        return 1 / (1 + Math.exp(-x));
    }
}