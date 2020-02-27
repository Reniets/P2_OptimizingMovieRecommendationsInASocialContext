package NeuralNetwork.Trainer;

import NeuralNetwork.ANN.ANN;
import NeuralNetwork.ANN.ActivationFunctions.SigmoidActivation;
import NeuralNetwork.ANN.Node;
import NeuralNetwork.ANN.Weight;
import NeuralNetwork.Trainer.Costs.MSECost;
import NeuralNetwork.Trainer.Methods.SGD;
import NeuralNetwork.Trainer.Trainer;
import NeuralNetwork.Trainer.TrainingData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TrainerTest {

    // Tests the constructor
    @Test
    void constructor01(){
        ANN network = new ANN(1,1,1,1, new SigmoidActivation());
        Trainer trainer = new Trainer(network, new SGD(new MSECost(),1));

        assertEquals(network, trainer.network);
    }

    // Tests the startTraining function
    @Test
    void train01(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ANN network = new ANN(1,1,1,1, new SigmoidActivation());
            Trainer trainer = new Trainer(network, new SGD(new MSECost(),0.5));

            ArrayList<TrainingData> data = new ArrayList<>(1);
            double[] inputs = {1,0};
            double[] outputs = {1};

            data.add(new TrainingData(inputs, outputs));

            trainer.startTraining(data,1);
        });
    }

    @Test
    void train02(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ANN network = new ANN(1,1,1,1, new SigmoidActivation());
            Trainer trainer = new Trainer(network, new SGD(new MSECost(),0.5));

            ArrayList<TrainingData> data = new ArrayList<>(1);
            double[] inputs = {1};
            double[] outputs = {1,0};

            data.add(new TrainingData(inputs, outputs));

            trainer.startTraining(data,1);
        });
    }

    double sigmoid(double x){
        return 1 / (1 + Math.exp(-x));
    }
    private double sigmoidPrime(double x){
        return x * (1 - x);
    }

    // 2 layers: 1 input & 1 outputs
    @Test
    void train03(){
        ANN network = new ANN(1,1,0,0, new SigmoidActivation());

        Weight W1 = network.layers.get(1).nodes.get(0).weights.get(0);
        W1.weight = 0.5;
        Trainer trainer = new Trainer(network, new SGD(new MSECost(),0.5));

        ArrayList<TrainingData> data = new ArrayList<>(1);
        double[] inputs = {1};
        double[] outputs = {1};
        data.add(new TrainingData(inputs, outputs));

        trainer.startTraining(data,1);

        double sigmoidOutput = network.layers.get(1).nodes.get(0).value;
        double expectedOutput = 0.5 - 0.5 * (sigmoidOutput - 1) * sigmoidPrime(sigmoidOutput) * (1);

        assertEquals(expectedOutput, W1.weight);
    }

    // 2 layers: 1 input & 2 outputs
    @Test
    void train04(){
        ANN network = new ANN(1,2,0,0, new SigmoidActivation());
        Weight W1 = network.layers.get(1).nodes.get(0).weights.get(0);
        Weight W2 = network.layers.get(1).nodes.get(1).weights.get(0);
        W1.weight = 0.5;
        W2.weight = 0.5;
        Trainer trainer = new Trainer(network, new SGD(new MSECost(),0.5));

        ArrayList<TrainingData> data = new ArrayList<>(1);
        double[] inputs = {1};
        double[] outputs = {1,1};
        data.add(new TrainingData(inputs, outputs));

        trainer.startTraining(data,1);

        double sigmoidOutput1 = network.layers.get(1).nodes.get(0).value;
        double expectedOutput1 = 0.5 - 0.5 * (sigmoidOutput1 - 1) * sigmoidPrime(sigmoidOutput1) * (1);

        double sigmoidOutput2 = network.layers.get(1).nodes.get(1).value;
        double expectedOutput2 = 0.5 - 0.5 * (sigmoidOutput2 - 1) * sigmoidPrime(sigmoidOutput2) * (1);

        assertEquals(expectedOutput1, W1.weight);
        assertEquals(expectedOutput2, W2.weight);
    }

    // 2 layers: 2 input & 1 outputs
    @Test
    void train05(){
        ANN network = new ANN(2,1,0,0, new SigmoidActivation());
        Weight W1 = network.layers.get(1).nodes.get(0).weights.get(0);
        Weight W2 = network.layers.get(1).nodes.get(0).weights.get(1);
        W1.weight = 0.5;
        W2.weight = 0.5;
        Trainer trainer = new Trainer(network, new SGD(new MSECost(),0.5));

        ArrayList<TrainingData> data = new ArrayList<>(1);
        double[] inputs = {1,1};
        double[] outputs = {1};
        data.add(new TrainingData(inputs, outputs));

        trainer.startTraining(data,1);

        double sigmoidOutput = network.layers.get(1).nodes.get(0).value;
        double expectedOutput = 0.5 - 0.5 * (sigmoidOutput - 1) * sigmoidPrime(sigmoidOutput) * (1);

        assertEquals(expectedOutput, W1.weight);
        assertEquals(expectedOutput, W2.weight);
    }

    // 3 layers: 1 input & 1 hidden & 1 output
    @Test
    void train06(){
        ANN network = new ANN(1,1,1,1, new SigmoidActivation());
        Weight W1 = network.layers.get(2).nodes.get(0).weights.get(1);
        Weight W2 = network.layers.get(1).nodes.get(0).weights.get(1);
        W1.weight = 0.5;
        W2.weight = 0.5;
        Trainer trainer = new Trainer(network, new SGD(new MSECost(),0.5));

        ArrayList<TrainingData> data = new ArrayList<>(1);
        double[] inputs = {1};
        double[] outputs = {1};
        data.add(new TrainingData(inputs, outputs));

        trainer.startTraining(data,1);

        // W1
        double w1SigmoidOutput = network.layers.get(2).nodes.get(0).value;
        double w1D = (w1SigmoidOutput - 1) * sigmoidPrime(w1SigmoidOutput) * (network.layers.get(1).nodes.get(0).value);
        double w1ExpectedOutput = 0.5 - 0.5 * w1D;

        // W2
        double w2D = network.layers.get(0).nodes.get(0).value * network.layers.get(1).nodes.get(0).effect;
        double w2ExpectedOutput = 0.5 - 0.5 * w2D;

        assertEquals(w1ExpectedOutput, W1.weight);
        assertEquals(w2ExpectedOutput, W2.weight);
    }


    @Test
    void effect01(){
        ANN network = new ANN(1,1,0,0, new SigmoidActivation());
        network.layers.get(1).nodes.get(0).weights.get(0).weight = 0.5;
        Trainer trainer = new Trainer(network, new SGD(new MSECost(),0.5));
        Node outputNode = network.layers.get(1).nodes.get(0);

        ArrayList<TrainingData> data = new ArrayList<>(1);
        double[] inputs = {1};
        double[] outputs = {1};
        data.add(new TrainingData(inputs, outputs));

        trainer.startTraining(data,1);

        double out = network.layers.get(1).nodes.get(0).value;

        double expected = (out - outputs[0]) * sigmoidPrime(out);

        assertEquals(expected, outputNode.effect);
    }

    @Test
    void effect02(){
        ANN network = new ANN(1,1,1,1, new SigmoidActivation());
        network.layers.get(2).nodes.get(0).weights.get(1).weight = 0.5;
        network.layers.get(1).nodes.get(0).weights.get(1).weight = 0.5;
        Trainer trainer = new Trainer(network, new SGD(new MSECost(),0.5));
        Node outputNode = network.layers.get(2).nodes.get(0);
        Node hiddenNode = network.layers.get(1).nodes.get(0);

        ArrayList<TrainingData> data = new ArrayList<>(1);
        double[] inputs = {1};
        double[] outputs = {1};
        data.add(new TrainingData(inputs, outputs));

        trainer.startTraining(data,1);

        double output = network.layers.get(2).nodes.get(0).value;
        double outHidden = network.layers.get(1).nodes.get(0).value;

        double expected1 = (output - outputs[0]) * sigmoidPrime(output);
        double expected2 = 0.5 * expected1 * sigmoidPrime(outHidden);

        assertEquals(expected1, outputNode.effect);
        assertEquals(expected2, hiddenNode.effect);
    }

}