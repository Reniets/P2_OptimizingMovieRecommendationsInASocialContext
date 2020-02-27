package NeuralNetwork.Trainer;

import NeuralNetwork.ANN.ANN;
import NeuralNetwork.Trainer.Methods.TrainingMethod;

import java.util.ArrayList;

public class Trainer {
    ANN network = null;
    private TrainingMethod TM;
    public double error = 0;
    private boolean printResult = false;

    public Trainer() {
    }

    public Trainer(ANN network, TrainingMethod TM) {
        this.network = network;
        this.TM = TM;
    }

    public TrainingMethod getTM() {
        return TM;
    }

    boolean getPrintResult(){
        return this.printResult;
    }

    public void setNetwork(ANN network) {
        this.network = network;
    }

    public void setTrainingMethod(TrainingMethod TM) {
        this.TM = TM;
    }

    public void printResult(boolean b){
        printResult = b;
    }


    // Method to startTraining a neural network with backpropagation
    public void startTraining(ANN network, TrainingMethod TM, ArrayList<TrainingData> data, int iterations) {

        // Make sure the dataSet fits the neural network.
        this.checkDataSize(network, data);

        for (int i = 0; i < iterations; i++){
            // Backpropagate over the whole data set.
            TM.train(network, data);

            this.error = TM.getErrorRate();

            if (printResult){
                // Print the avg error
                this.printAvgError(this.error);

                // Print the current progress (1% at a time)
                this.printProgress(i, iterations);
            }

        }
    }

    public void startTraining(ArrayList<TrainingData> data, int iterations){
        this.startTraining(this.network, this.TM, data, iterations);
    }

    private void printAvgError(double avgError){
        System.out.println( "Error: " + avgError);
    }

    private void printProgress(int i, int iterations){
        if (i != 0 && i % (iterations/100) == 0){
            System.out.println((int) Math.ceil( ((double) i / (double) iterations) * 100 )  + "%");
        }
    }

    private void checkDataSize(ANN network, ArrayList<TrainingData> data){
        // Check for error in Data.
        int inputLayerSize = network.layers.get(0).nodes.size();
        int outputLayerSize = network.layers.get(network.layers.size() - 1).nodes.size();

        // For every dataPoint
        for (TrainingData currentDataPoint : data) {
            boolean inputSizeFits = currentDataPoint.inputs.length == inputLayerSize;
            boolean outputSizeFits = currentDataPoint.outputs.length == outputLayerSize;

            if ( !(inputSizeFits && outputSizeFits) ) {
                throw new IllegalArgumentException("Some data does not have the correct size to fit the network");
            }
        }
    }
}
