package NeuralNetwork.ANN;

import java.util.Random;

public class Weight {
    public final Node startNode;
    public double weight;
    public double updateValue; // Used to store a temporary new weight - Maybe another solution is needed

    // Constructor if you want a random weight value.
    public Weight(Node startNode, Random r){
        this(startNode, 1, r);
    }

    // Constructor if you want a specific weight limit.
    public Weight(Node startNode, double weightDeviation, Random r){
        this.startNode = startNode;
        this.updateValue = 0;

        this.weight = r.nextGaussian() * weightDeviation;    // Random value with mean 0 and standard deviation of "weightDeviation"
    }

    public void updateWeight(){
        this.weight = this.updateValue;
    }
}
