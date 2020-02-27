package NeuralNetwork.Trainer;

public class TrainingData {
    public final double[] inputs;
    public final double[] outputs;

    public TrainingData(double[] inputs, double[] outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
    }
}
