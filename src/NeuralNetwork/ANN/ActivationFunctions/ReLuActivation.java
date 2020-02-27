package NeuralNetwork.ANN.ActivationFunctions;

public class ReLuActivation implements ActivationFunction {
    private final int ID = 2;

    @Override
    public double activation(double x) {
        return Math.max(x,0);
    }

    @Override
    public double activationPrime(double x) {
        return (x > 0) ? 1 : 0;
    }

    @Override
    public int getID() {
        return this.ID;
    }
}
