package NeuralNetwork.ANN.ActivationFunctions;

public class ActivationFunctions {

    public static ActivationFunction getByID(int ID){
        switch (ID){
            case 0: return new SigmoidActivation();
            case 1: return new TanhActivation();
            case 2: return new ReLuActivation();

            // Break is not required since we call return, in every case.
            default: throw new IllegalArgumentException("No activation function with this ID");
        }
    }

}
