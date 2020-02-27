package NeuralNetwork.ANN.ActivationFunctions;

public interface ActivationFunction {

    // Used for feed forward
    double activation(double x);

    // Used for backpropagation. (This x is equal to the output of the "activation" function above, and NOT the input)
    double activationPrime(double x);

    int getID();
}
