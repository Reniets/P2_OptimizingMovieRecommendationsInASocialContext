package NeuralNetwork.ANN;

import NeuralNetwork.ANN.Node;
import NeuralNetwork.ANN.Weight;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class WeightTest {

    @Test
    void updateValue01(){
        Random r = new Random();

        Node node = new Node();
        Weight W = new Weight(node, r);

        assertEquals(0, W.updateValue);
    }

    @Test
    void updateValue02(){
        Random r = new Random();

        Node node = new Node();
        Weight W = new Weight(node, r);
        W.updateValue = 1;

        W.updateWeight();

        assertEquals(1, W.weight);
    }
}