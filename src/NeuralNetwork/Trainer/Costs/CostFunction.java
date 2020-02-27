package NeuralNetwork.Trainer.Costs;

public interface CostFunction {

    // The cost function, used to calculate error.
    double cost(double target, double prediction);

    // The cost prime funciton, used in backpropagation.
    double costPrime(double target, double prediction);
}
