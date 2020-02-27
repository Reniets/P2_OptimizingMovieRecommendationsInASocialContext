package NeuralNetwork.Trainer.Methods;

import NeuralNetwork.ANN.ANN;
import NeuralNetwork.Trainer.Costs.CostFunction;
import NeuralNetwork.Trainer.Regularization.Cost.L_Regularization;
import NeuralNetwork.Trainer.TrainingData;

import java.util.ArrayList;

public class SGD extends TrainingMethod {
    private final MiniBatch MB;

    public SGD(CostFunction CF, double learningRate){
        this(CF, learningRate, null);
    }

    public SGD(CostFunction CF, double learningRate, L_Regularization L_Reg){
        super(CF);
        this.MB = new MiniBatch(CF, learningRate, 1, L_Reg);
    }

    public void setLearningRate(double learningRate) {
        // Set new learning rate
        this.MB.setLearningRate(learningRate);
    }

    @Override
    public void train(ANN network, ArrayList<TrainingData> data) {
        MB.train(network, data);
    }

}
