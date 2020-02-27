package Predictors;

import NeuralNetwork.ImportExportANN;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PredictHybridTest {

    @Test
    void prediction01() {

        ImportExportANN IEANN = new ImportExportANN();

        PredictContent content = new PredictContent(3, IEANN.importANN("Tests/Predictors/testData/PredictContent/simpleANN.csv"));
        PredictCollaborate collaborate = new PredictCollaborate(3, 1700);

        PredictHybrid hybrid = new PredictHybrid(collaborate, content, 0.5);

        double expected = content.predictRating(1,1) * 0.5 + collaborate.predictRating(1,1) * 0.5;
        double prediction = hybrid.predictRating(1,1);

        Assertions.assertEquals(expected, prediction, 0.001);
    }
}
