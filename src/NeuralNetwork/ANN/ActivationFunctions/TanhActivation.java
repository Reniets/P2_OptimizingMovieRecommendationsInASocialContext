package NeuralNetwork.ANN.ActivationFunctions;

public class TanhActivation implements ActivationFunction {
    private final int ID = 1;
    private final ActivationFunction sigmoid = new SigmoidActivation();

    @Override
    public double activation(double x) {
        return Math.tanh(x);
    }

    @Override
    public double activationPrime(double x) {
        return sigmoid.activationPrime(tanhInverse(x) * 2) * 4;
    }

    @Override
    public int getID() {
        return this.ID;
    }

    private double tanhInverse(double x){
        return (0.5 * (Math.log(1+x) - Math.log(1-x)));
    }

}
