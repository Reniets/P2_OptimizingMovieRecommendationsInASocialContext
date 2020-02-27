package NeuralNetwork.ANN.ActivationFunctions;

public class SigmoidActivation implements ActivationFunction {
    private final int ID = 0;

    @Override
    public double activation(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    @Override
    public double activationPrime(double x) {
        return x * (1 - x);
    }

    @Override
    public int getID() {
        return this.ID;
    }
}
