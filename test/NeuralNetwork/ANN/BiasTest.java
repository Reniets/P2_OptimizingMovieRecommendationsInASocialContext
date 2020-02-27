package NeuralNetwork.ANN;

import NeuralNetwork.ANN.ANN;
import NeuralNetwork.ANN.ActivationFunctions.SigmoidActivation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BiasTest {

    @Test
    void Bias01() {
        ANN network = new ANN(5,5,1,2, new SigmoidActivation());

        for (int i = 0; i < 5; i++){
            assertEquals(network.bias, network.layers.get(2).nodes.get(i).weights.get(0).startNode);
        }

        for (int i = 0; i < 2; i++){
            assertEquals(network.bias, network.layers.get(1).nodes.get(i).weights.get(0).startNode);
        }
    }

    @Test
    void Bias02(){
        ANN network = new ANN(1,1,0,0, new SigmoidActivation());

        assertEquals(1, network.bias.value);
    }
}