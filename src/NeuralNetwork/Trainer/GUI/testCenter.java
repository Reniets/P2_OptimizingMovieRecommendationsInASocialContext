package NeuralNetwork.Trainer.GUI;

import CSV.CSV_Reader;
import NeuralNetwork.Trainer.TrainingData;
import Other.DataImporter;
import Profiles.Profile;
import Profiles.ProfileGenerator;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;

public class testCenter extends Application {
    public ArrayList<TrainingData> trainingData = new ArrayList<>();
    public ArrayList<TrainingData> testData = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        // Prepare root
        HBox root = new HBox(25);
        root.setAlignment(Pos.CENTER);

        // Append charts to root
        ANN_Graph ann_graph1 = new ANN_Graph(this);
        ANN_Graph ann_graph2 = new ANN_Graph(this);
        ANN_Graph ann_graph3 = new ANN_Graph(this);
        ANN_Graph ann_graph4 = new ANN_Graph(this);

        root.getChildren().add(ann_graph1.getFX());
        root.getChildren().add(ann_graph2.getFX());
        root.getChildren().add(ann_graph3.getFX());
        root.getChildren().add(ann_graph4.getFX());

        // Prepare scene and show it
        Scene scene = new Scene(root);
        scene.getStylesheets().add("NeuralNetwork/Trainer/GUI/CSS/ANN_Graph.css");
        stage.setScene(scene);
        stage.setTitle("ANN Training GUI");
        stage.show();

        // ------- Program logic -------

        int sepID = 0;

        // Import movieProfiles
        HashMap<Integer, Profile> movieProfileHashMap = DataImporter.importMovieProfileList(sepID);

        // Import userProfiles
        HashMap<Integer, Profile> userProfileHashMap = DataImporter.importUserProfileList(sepID);

        // Import training data
        this.trainingData = this.importData("data/sep/0/NyTrain_0.csv", movieProfileHashMap, userProfileHashMap);

        // Import validate data
        this.testData = this.importData("data/sep/0/Test_0.csv", movieProfileHashMap, userProfileHashMap);

    }

    private ArrayList<TrainingData> importData(String filePath, HashMap<Integer, Profile> movieProfileHashMap, HashMap<Integer, Profile> userProfileHashMap){
        CSV_Reader CSVR = new CSV_Reader(filePath);

        int inputSize = (userProfileHashMap.get(1).convertToArray().length - 1) * 2;

        ArrayList<TrainingData> data = new ArrayList<>(CSVR.getTotalLines());

        // Loads training points
        for (ArrayList<String> readLine : CSVR) {

            // Extract data from rating line
            int userID = Integer.parseInt(readLine.get(0));
            int movieID = Integer.parseInt(readLine.get(1));
            double rating = Double.parseDouble(readLine.get(2)) / 5;

            // Convert data to trainingData format
            double[] input = new double[inputSize];
            double[] output = {rating};

            double[] userSrc = userProfileHashMap.get(userID).convertToArray();
            double[] movieSrc = movieProfileHashMap.get(movieID).convertToArray();
            System.arraycopy(userSrc, 1, input, 0, userSrc.length - 1);
            System.arraycopy(movieSrc, 1, input, userSrc.length - 1, movieSrc.length - 1);

            data.add(new TrainingData(input, output));
        }

        return data;
    }

}
