package Predictors;

import CSV.CSV_Reader;
import Other.DataImporter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

public class Predictor {
    private final int fileNumber;
    private final HashMap<Integer, HashMap<Integer, Double>> seenBy;
    private final HashSet<Integer> movieIDS;

    public Predictor(int fileNumber) {
        this.fileNumber = fileNumber;
        this.seenBy = DataImporter.importSeenBy(fileNumber);
        this.movieIDS = DataImporter.importMovieIDS();
    }

    public HashMap<String, Double> validate(PredictionMethod predictionMethod){
        // Get filepath to test_x.csv
        String filePathTest = "data/sep/" + this.fileNumber + "/Test_" + this.fileNumber + ".csv";

        CSV_Reader CSVR_Test = new CSV_Reader(filePathTest);

        // Convert the name of the predictor method to a string
        String predictorName = predictionMethod.getClass().getSimpleName();

        int userID, movieID, lineNumber = 0;
        double target, prediction, difference, errorsForMAE = 0, errorsForRMSE = 0;

        // Loop through all test points and accumulate errors
        for (ArrayList<String> readTestPoint : CSVR_Test){
            // Read from validate file
            userID = Integer.parseInt(readTestPoint.get(0));
            movieID = Integer.parseInt(readTestPoint.get(1));
            target = Double.parseDouble(readTestPoint.get(2));

            // Check to see if user has already rated movie, then return previous rating
            if(seenBy.get(userID).containsKey(movieID))
                prediction = seenBy.get(userID).get(movieID);
            else
                prediction = predictionMethod.predictRating(userID, movieID);

            // Calculate errors
            difference = target - prediction;

            errorsForMAE += Math.abs(difference);
            errorsForRMSE += difference * difference;

            if(++lineNumber % 10000 == 0) {
                System.out.println("Tested " + predictorName + ": " + lineNumber);
            }
        }

        ArrayList<Double> errors = new ArrayList<>();
        HashMap<String, Double> output = new HashMap<>(2);

        int numberOfTestPoints = CSVR_Test.getTotalLines();

        // Calculate the two measures of error
        double MAE = errorsForMAE / numberOfTestPoints;
        double RMSE = Math.sqrt(errorsForRMSE / numberOfTestPoints);

        errors.add(MAE);
        errors.add(RMSE);

        output.put("MAE", MAE);
        output.put("RMSE", RMSE);

        printErrorsToFile(errors, predictorName);

        return output;
    }

    private void printErrorsToFile(ArrayList<Double> errors, String predictorName) {
        DecimalFormat df = new DecimalFormat("0.000", DecimalFormatSymbols.getInstance(Locale.US));

        // Get filepath to where correlations should be exported to
        String filePath = "data/sep/" + this.fileNumber + "/Errors" + "_" + predictorName + "_" + this.fileNumber + ".csv";

        try {
            // Make file writer to specified path
            FileWriter fileWriter = new FileWriter(filePath, false);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            // Make prints
            printWriter.println(predictorName + " errors: ");
            printWriter.println("MAE: " + df.format(errors.get(0)));
            printWriter.println("RMSE: " + df.format(errors.get(1)));

            printWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportPredictions(PredictionMethod predictionMethod) {
        double prediction;

        // Get filepath to where correlations should be exported to
        String filePath = "data/sep/" + this.fileNumber + "/collaboratePredictions_" + this.fileNumber + ".csv";

        try {
            // Prepare printWriter to write in specified file.
            FileWriter fileWriter = new FileWriter(filePath, false);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            for (Map.Entry<Integer, HashMap<Integer, Double>> fromUser : this.seenBy.entrySet()) {
                for (Integer toMovie : this.movieIDS) {
                    // Check to see if user has already rated movie, then return previous rating
                    if(seenBy.get(fromUser.getKey()).containsKey(toMovie))
                        prediction = seenBy.get(fromUser.getKey()).get(toMovie);
                    else
                        prediction = predictionMethod.predictRating(fromUser.getKey(), toMovie);

                    printWriter.print(prediction);
                }
            }

            printWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
