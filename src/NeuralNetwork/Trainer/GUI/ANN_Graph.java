package NeuralNetwork.Trainer.GUI;

import NeuralNetwork.ANN.ANN;
import NeuralNetwork.ANN.ActivationFunctions.SigmoidActivation;
import NeuralNetwork.ImportExportANN;
import NeuralNetwork.Trainer.Costs.MSECost;
import NeuralNetwork.Trainer.Methods.MiniBatch;
import NeuralNetwork.Trainer.Trainer;
import NeuralNetwork.Trainer.TrainingData;
import Other.MovingAverage;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ANN_Graph {
    // FX elements
    private LineChart<Number, Number> chart;
    private HBox statsBar;
    private VBox settingsBar;
    private final HashMap<String, TextField> settings = new HashMap<>();
    private final testCenter Super;
    private final VBox FX;

    // Thread
    private Thread worker;
    private boolean currentlyTraining = false;

    // ANN and trainer objects
    private ANN network;
    private Trainer trainer = new Trainer();

    // ANN and trainer settings
    private int seed;
    private int testRate;
    private int iterationRate;
    private int totalIterations;
    private AtomicInteger currentIteration;

    // Early stopping
    private int stopCounter = 0;
    private double minTestScore = Double.MAX_VALUE;

    // Stats variables
    private double minCost;
    private MovingAverage avgTime = new MovingAverage();
    private int totalDatapoints = 0;

    /**************************************************************************
     *                                                                        *
     * Constructor.                                                           *
     *                                                                        *
     *************************************************************************/

    public ANN_Graph(testCenter Super) {
        this.Super = Super;

        // Prepare chart
        this.generateChart();
        this.generateStatBar();
        this.generateSettingsBar();

        this.FX = new VBox(5);
        this.FX.getStyleClass().add("ANN_Graph_VBox");

        // Add chart to root + separator
        this.FX.getChildren().add(this.chart);             //  Chart
        this.FX.getChildren().add(new Separator());        //Separator
        this.FX.getChildren().add(this.statsBar);          //  Stats bar
        this.FX.getChildren().add(new Separator());        //Separator
        this.FX.getChildren().add(this.settingsBar);       //  Settings
        this.FX.getChildren().add(new Separator());        //Separator
        this.FX.getChildren().add(generateButtonsBar());   //  Buttons

        // Prepare series
        XYChart.Series<Number, Number> series_Train = getSeriesTrain();
        XYChart.Series<Number, Number> series_Test = getSeriesTest();

        // Add series to charts
        this.chart.getData().add(series_Train);
        this.chart.getData().add(series_Test);
    }

    /**************************************************************************
     *                                                                        *
     * Functional methods.                                                    *
     * These functions have the purpose of controlling the behaviour of the   *
     * FX element.                                                            *
     *                                                                        *
     *************************************************************************/

    void startTraining() {

        // Load all settings

        // Seed
        this.seed = (int) readSettingsValue("Seed:");

        // Network size
        int inputs = (int) readSettingsValue("Inputs:");
        int outputs = (int) readSettingsValue("Outputs:");
        int hiddenLayers = (int) readSettingsValue("Hidden layers:");
        int hiddenNodes = (int) readSettingsValue("Hidden nodes:");

        // Graph settings
        this.iterationRate = (int) readSettingsValue("Training rate:");
        this.testRate = (int) readSettingsValue("Test rate:");

        // Trainer settings
        this.totalIterations = (int) readSettingsValue("Iterations:");
        int batchSize = (int) readSettingsValue("Batch size:");
        double learningRate = readSettingsValue("Learning rate:");

        // Initialize network and trainer
        this.network = new ANN(inputs, outputs, hiddenLayers, hiddenNodes, new SigmoidActivation(), this.seed);

        MiniBatch miniBatch = new MiniBatch(new MSECost(), learningRate, batchSize);
        this.trainer = new Trainer(this.network, miniBatch);

        // Prepare thread
        this.worker = new Thread(() -> {

            // Reset all previous training sessions
            this.currentIteration = new AtomicInteger(0);
            this.minCost = Double.MAX_VALUE;

            this.chart.getData().get(0).getData().clear();
            this.chart.getData().get(1).getData().clear();

            this.avgTime.reset();
            this.totalDatapoints = 0;

            this.stopCounter = 0;
            this.minTestScore = Double.MAX_VALUE;

            this.currentlyTraining = true;  // Sets the training flag to true, and allow training to take place

            while (this.currentlyTraining && this.currentIteration.get() < this.totalIterations) {

                long startTime;
                long endTime;

                try {
                    startTime = System.currentTimeMillis();
                    this.trainer.startTraining(this.Super.trainingData, 1);
                    endTime = System.currentTimeMillis();
                } catch (IllegalArgumentException e){
                    e.printStackTrace();
                    this.currentlyTraining = false;
                    return;
                }

                boolean iterationRateReached = this.currentIteration.get() % this.iterationRate == 0;
                boolean testRateReached = this.currentIteration.get() % this.testRate == 0;
                boolean trainingEndReached = this.currentIteration.get() == this.totalIterations - 1;

                int X = this.currentIteration.incrementAndGet();

                if (iterationRateReached || trainingEndReached) {
                    this.chart.getData().get(0).getData().add(new XYChart.Data<>(X, this.trainer.error));    // Training
                }

                if (testRateReached || trainingEndReached) {
                    double testError = performTest();

                    this.chart.getData().get(1).getData().add(new XYChart.Data<>(X, testError));
                    this.updateOverfitting(Math.abs(testError - this.trainer.error));

                    // Early stopping
                    if (this.minTestScore > testError){
                        this.minTestScore = testError;
                        this.stopCounter = 0;
                    } else {
                        this.stopCounter++;

                        if (this.stopCounter >= 10){
                            this.buttonEventStop();
                        }
                    }
                }

                if (trainingEndReached) {
                    this.currentlyTraining = false;
                }

                Platform.runLater(() -> {
                    this.updateMinCost(trainer.error);

                    avgTime.add(endTime-startTime);
                    this.updateAvgTime();

                    this.updateTotalDataPoints();
                });
            }
        });

        this.worker.setDaemon(true);  // Makes sure the thread will stop, if we close the window
        this.worker.start();          // Start the thread
    }

    // Predict on validate data
    private double performTest(){
        ArrayList<TrainingData> testData = this.Super.testData;

        double avgError = 0;
        MSECost mseCost = new MSECost();

        for (TrainingData data : testData){
            ArrayList<Double> ANN_Output = this.network.evaluateInputs(data.inputs);

            // Calc error, and accumulate
            for (int i = 0; i < ANN_Output.size(); i++){
                double target = data.outputs[i];
                double prediction = ANN_Output.get(i);

                avgError += mseCost.cost(target, prediction);
            }
        }

        return avgError / testData.size();
    }

    // Update stats methods

    private void updateMinCost(double cost){

        if (cost < this.minCost){
            this.minCost = cost;

            VBox vBox = (VBox) this.statsBar.getChildren().get(0);
            Text text = (Text) vBox.getChildren().get(1);

            String costString = String.format("%.4f", cost);

            text.setText(costString);
        }
    }

    private void updateAvgTime(){
        VBox vBox = (VBox) this.statsBar.getChildren().get(1);
        Text text = (Text) vBox.getChildren().get(1);

        double avgSec = avgTime.getAverage() / 1000;

        String avgString = String.format("%.2f sec", avgSec);

        text.setText(avgString);
    }

    private void updateTotalDataPoints(){
        this.totalDatapoints += this.Super.trainingData.size();

        VBox vBox = (VBox) this.statsBar.getChildren().get(2);
        Text text = (Text) vBox.getChildren().get(1);

        String datapointsString = String.valueOf(this.totalDatapoints);

        text.setText(datapointsString);
    }

    private void updateOverfitting(double overfitFactor){
        VBox vBox = (VBox) this.statsBar.getChildren().get(3);
        Text text = (Text) vBox.getChildren().get(1);

        String overfitString = String.format("%.4f", overfitFactor);

        text.setText(overfitString);
    }

    private double readSettingsValue(String setting) {
        return Double.parseDouble(this.settings.get(setting).getText());
    }

    /**************************************************************************
     *                                                                        *
     * Button event methods.                                                  *
     * These methods are used for controlling the buttons event actions.      *
     *                                                                        *
     *************************************************************************/

    private void buttonEventStart() {
        if (!this.currentlyTraining) {
            this.startTraining();
        }
    }

    private void buttonEventStop() {
        this.currentlyTraining = false; // Sets the flag to false, and stops all training

        if (this.worker != null) {
            this.worker.interrupt();
        }
    }

    private void buttonEventExport() {

        // Get directory to save export data
        JFileChooser f = new JFileChooser();
        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        f.showSaveDialog(null);

        // Export snapshot of graph and settings
        WritableImage image = this.FX.snapshot(new SnapshotParameters(), null);
        File file = new File(f.getSelectedFile().toString() + "/Snapshot.png");

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Export ANN
        ImportExportANN IEANN = new ImportExportANN();
        IEANN.exportANN(this.network, f.getSelectedFile().toString(), "/ANN.csv");

        // Export graph data
        String filePathTrain = f.getSelectedFile().toString() + "/dataPoints_Train.csv";
        String filePathTest = f.getSelectedFile().toString() + "/dataPoints_Test.csv";

        XYChart.Series<Number, Number> seriesTrain = this.chart.getData().get(0);
        XYChart.Series<Number, Number> seriesTest = this.chart.getData().get(1);

        this.exportData(filePathTrain, seriesTrain);
        this.exportData(filePathTest, seriesTest);

    }

    private void exportData(String filePath, XYChart.Series<Number, Number> series){

        try {
            // Prepare printWriter to write in specified file.
            FileWriter fileWriter = new FileWriter(filePath, false);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            int totalDataPoints = series.getData().size();

            // X cords
            for (int i = 0; i < totalDataPoints; i++){
                Number X = series.getData().get(i).getXValue();

                boolean lastDataPointReached = i == totalDataPoints - 1;

                if (lastDataPointReached){
                    printWriter.println(X);
                } else {
                    printWriter.print(X + ",");
                }
            }

            // Y cords
            for (int i = 0; i < totalDataPoints; i++){
                Number Y = series.getData().get(i).getYValue();

                boolean lastDataPointReached = i == totalDataPoints - 1;

                if (lastDataPointReached){
                    printWriter.print(Y);
                } else {
                    printWriter.print(Y + ",");
                }
            }

            printWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**************************************************************************
     *                                                                        *
     * FX element constructor methods.                                        *
     * These methods are used for constructing the correct FX element, and    *
     * serve no other running functional purpose.                             *
     *                                                                        *
     *************************************************************************/

    VBox getFX() {
        return this.FX;
    }

    private HBox generateButtonsBar() {
        HBox buttonWrapper = new HBox(50);
        buttonWrapper.setAlignment(Pos.CENTER);

        Button buttonStart = new Button("Start");
        Button buttonStop = new Button("Stop");
        Button buttonExport = new Button("Export");

        buttonStart.setOnMouseClicked(event -> this.buttonEventStart());

        buttonStop.setOnMouseClicked(event -> this.buttonEventStop());

        buttonExport.setOnMouseClicked(event -> this.buttonEventExport());

        buttonWrapper.getChildren().add(buttonStart);
        buttonWrapper.getChildren().add(buttonStop);
        buttonWrapper.getChildren().add(buttonExport);

        return buttonWrapper;
    }

    private void generateSettingsBar() {
        this.settingsBar = new VBox(10);

        ArrayList<HBox> layers = new ArrayList<>();

        // Prepare layers
        layers.add(generateSettingsLayer(new ArrayList<>(Arrays.asList("Inputs:", "Outputs:", "Hidden layers:", "Hidden nodes:"))));
        layers.add(generateSettingsLayer(new ArrayList<>(Arrays.asList("Iterations:", "Batch size:", "Training rate:", "Test rate:"))));
        layers.add(generateSettingsLayer(new ArrayList<>(Arrays.asList("Learning rate:", "Seed:"))));

        // Apply padding
        for (HBox layer : layers) {
            layer.getStyleClass().add("padding_5");
            this.settingsBar.getChildren().add(layer);
        }
    }

    private HBox generateSettingsLayer(ArrayList<String> options) {
        // Prepare layer
        HBox layer = new HBox(5);
        layer.setAlignment(Pos.CENTER);

        // Make room for the options and set correct alignment

        for (String option : options) {
            VBox tempBox = new VBox();
            tempBox.setAlignment(Pos.CENTER);

            tempBox.getChildren().add(new Text(option));
            tempBox.getChildren().add(new TextField());

            this.settings.put(option, (TextField) tempBox.getChildren().get(1));             // For easier access

            layer.getChildren().add(tempBox);
        }

        // Return layer
        return layer;
    }

    private void generateChart() {
        this.chart = new LineChart<>(getAxisX(), getAxisY());
        this.chart.setCreateSymbols(false);
        this.chart.setAnimated(false);
    }

    private void generateStatBar() {
        this.statsBar = new HBox(50);

        statsBar.getStyleClass().add("Padding_5");
        statsBar.setAlignment(Pos.CENTER);

        VBox stat1 = new VBox();
        stat1.setAlignment(Pos.CENTER);
        stat1.getChildren().add(new Text("Min cost:"));
        stat1.getChildren().add(new Text("0.0"));

        VBox stat2 = new VBox();
        stat2.setAlignment(Pos.CENTER);
        stat2.getChildren().add(new Text("Avg. Time: "));
        stat2.getChildren().add(new Text("0 sec"));

        VBox stat3 = new VBox();
        stat3.setAlignment(Pos.CENTER);
        stat3.getChildren().add(new Text("Total datapoints: "));
        stat3.getChildren().add(new Text("0"));

        VBox stat4 = new VBox();
        stat4.setAlignment(Pos.CENTER);
        stat4.getChildren().add(new Text("Overfitting: "));
        stat4.getChildren().add(new Text("0.0"));

        statsBar.getChildren().add(stat1);
        statsBar.getChildren().add(stat2);
        statsBar.getChildren().add(stat3);
        statsBar.getChildren().add(stat4);
    }

    private XYChart.Series<Number, Number> getSeriesTrain() {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Training");
        return series;
    }

    private XYChart.Series<Number, Number> getSeriesTest() {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Test");
        return series;
    }

    private NumberAxis getAxisX() {
        final NumberAxis xAxis = new NumberAxis();
        xAxis.setAnimated(false);
        xAxis.setLabel("Iterations");

        return xAxis;
    }

    private NumberAxis getAxisY() {
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setAnimated(false);
        yAxis.setLabel("Cost");

        return yAxis;
    }
}
