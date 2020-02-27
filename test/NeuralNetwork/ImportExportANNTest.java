package NeuralNetwork;

import NeuralNetwork.ANN.ANN;
import NeuralNetwork.ANN.ActivationFunctions.SigmoidActivation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImportExportANNTest {

    @Test
    void exportANN01(){
            ImportExportANN exporter = new ImportExportANN();
            ANN network = new ANN(4,2,3,3, new SigmoidActivation());

            boolean testSuccess = exporter.exportANN(network,"export","network.csv");
            assertTrue(testSuccess);
    }

    @Test
    void importANN01(){
        ImportExportANN IEA = new ImportExportANN();
        ANN network = new ANN(1,2,0,0, new SigmoidActivation());

        IEA.exportANN(network, "export", "testExport.csv");
        ANN newNetwork = IEA.importANN("export/testExport.csv");

        double weightOld, weightNew;

        for (int nodeID = 0; nodeID < 2; nodeID++){
            for (int weightID = 0; weightID < 2; weightID++){

                weightOld = network.layers.get(1).nodes.get(nodeID).weights.get(weightID).weight;
                weightNew = newNetwork.layers.get(1).nodes.get(nodeID).weights.get(weightID).weight;


                assertEquals(weightOld, weightNew);
            }
        }
    }
}