package NeuralNetwork.Trainer.Costs;

public class MSECost implements CostFunction {

    @Override
    public double cost(double target, double prediction) {
        return 0.5 * Math.pow(target - prediction,2);
    }

    @Override
    public double costPrime(double target, double prediction) {
        return (prediction - target);
    }
}
