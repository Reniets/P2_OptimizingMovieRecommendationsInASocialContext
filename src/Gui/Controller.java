package Gui;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static Other.DataImporter.*;

public class Controller {

    @FXML
    private AnchorPane LogInPage;

    @FXML
    private TextField UserInput;

    @FXML
    private PasswordField PasswordInput;

    @FXML
    private VBox FrontPage;

    @FXML
    private AnchorPane CreateProfile;

    @FXML
    private TextField UsernameInput;

    @FXML
    private AnchorPane ProfilePage;

    @FXML
    private Text ProfileID;

    @FXML
    private Text MovieText0;

    @FXML
    private Text MovieText1;

    @FXML
    private Text MovieText2;

    @FXML
    private Text MovieText3;

    @FXML
    private Text MovieText4;

    @FXML
    private Text MovieText5;

    @FXML
    private Text MovieText6;

    @FXML
    private Text MovieText7;

    @FXML
    private Text MovieText8;

    @FXML
    private Text MovieText9;

    @FXML
    private Text MovieTitle0;

    @FXML
    private Text MovieTitle1;

    @FXML
    private Text MovieTitle2;

    @FXML
    private Text MovieTitle3;

    @FXML
    private Text MovieTitle4;

    @FXML
    private Text MovieTitle5;

    @FXML
    private Text MovieTitle6;

    @FXML
    private Text MovieTitle7;

    @FXML
    private Text MovieTitle8;

    @FXML
    private Text MovieTitle9;

    @FXML
    private Text Rating0;

    @FXML
    private Text Rating1;

    @FXML
    private Text Rating2;

    @FXML
    private Text Rating3;

    @FXML
    private Text Rating4;

    @FXML
    private Text Rating5;

    @FXML
    private Text Rating6;

    @FXML
    private Text Rating7;

    @FXML
    private Text Rating8;

    @FXML
    private Text Rating9;

    @FXML
    private Button SimilarUser0;

    @FXML
    private Button SimilarUser1;

    @FXML
    private Button SimilarUser2;

    @FXML
    private Button SimilarUser3;

    @FXML
    private Button SimilarUser4;

    @FXML
    private Button SimilarUser5;

    @FXML
    private Button SimilarUser6;

    @FXML
    private Button SimilarUser7;

    @FXML
    private Button SimilarUser8;

    @FXML
    private Button SimilarUser9;

    @FXML
    private Text Correlation0;

    @FXML
    private Text Correlation1;

    @FXML
    private Text Correlation2;

    @FXML
    private Text Correlation3;

    @FXML
    private Text Correlation4;

    @FXML
    private Text Correlation5;

    @FXML
    private Text Correlation6;

    @FXML
    private Text Correlation7;

    @FXML
    private Text Correlation8;

    @FXML
    private Text Correlation9;


    @FXML
    private void logInButtonClicked() throws IOException {
        LogInPage.setVisible(false);
        FrontPage.setVisible(true);
        int userID = Integer.parseInt(UserInput.getText());
        displayContent(userID);
        UserInput.clear();
        PasswordInput.clear();
    }

    @FXML
    private void logOutButtonClicked() {
        FrontPage.setVisible(false);
        LogInPage.setVisible(true);
    }

    @FXML
    private void createProfileButtonClicked() {
        LogInPage.setVisible(false);
        CreateProfile.setVisible(true);
        UserInput.clear();
        PasswordInput.clear();
    }

    @FXML
    private void cancelButtonClicked() {
        CreateProfile.setVisible(false);
        LogInPage.setVisible(true);
        UsernameInput.clear();
    }

    @FXML
    private void profileButtonClicked() throws IOException {
        FrontPage.setVisible(false);
        ProfilePage.setVisible(true);
    }

    @FXML
    private void backButtonClicked() {
        ProfilePage.setVisible(false);
        FrontPage.setVisible(true);
    }

    @FXML
    private void similarUser0Clicked() throws IOException {
        displayContent(Integer.parseInt(SimilarUser0.getText()));
    }

    @FXML
    private void similarUser1Clicked() throws IOException {
        displayContent(Integer.parseInt(SimilarUser1.getText()));
    }

    @FXML
    private void similarUser2Clicked() throws IOException {
        displayContent(Integer.parseInt(SimilarUser2.getText()));
    }

    @FXML
    private void similarUser3Clicked() throws IOException {
        displayContent(Integer.parseInt(SimilarUser3.getText()));
    }

    @FXML
    private void similarUser4Clicked() throws IOException {
        displayContent(Integer.parseInt(SimilarUser4.getText()));
    }

    @FXML
    private void similarUser5Clicked() throws IOException {
        displayContent(Integer.parseInt(SimilarUser5.getText()));
    }

    @FXML
    private void similarUser6Clicked() throws IOException {
        displayContent(Integer.parseInt(SimilarUser6.getText()));
    }

    @FXML
    private void similarUser7Clicked() throws IOException {
        displayContent(Integer.parseInt(SimilarUser7.getText()));
    }

    @FXML
    private void similarUser8Clicked() throws IOException {
        displayContent(Integer.parseInt(SimilarUser8.getText()));
    }

    @FXML
    private void similarUser9Clicked() throws IOException {
        displayContent(Integer.parseInt(SimilarUser9.getText()));
    }

    @FXML
    private void displayContent(int userID) throws IOException {
        displayRecommendedToUser(userID);
        displaySimilarUsers(userID);
    }

    @FXML
    private void displayRecommendedToUser(int userID) throws IOException {
        HashMap<Integer, String> titleMap = importTitles();
        LinkedHashMap<Integer, Double> predictedRatings = importRatings(userID);

        ArrayList<String> ratedByUserHistory = displayRatedByUser(userID);
        List<String> titleList = new ArrayList<>();

        for (int movieID : predictedRatings.keySet()) {

            if (titleList.size() >= 10) {
                break;
            }

            if (!(ratedByUserHistory.contains(titleMap.get(movieID)))) {
                titleList.add(titleMap.get(movieID));
            }
        }

        MovieText0.setText(titleList.get(0));
        MovieText1.setText(titleList.get(1));
        MovieText2.setText(titleList.get(2));
        MovieText3.setText(titleList.get(3));
        MovieText4.setText(titleList.get(4));
        MovieText5.setText(titleList.get(5));
        MovieText6.setText(titleList.get(6));
        MovieText7.setText(titleList.get(7));
        MovieText8.setText(titleList.get(8));
        MovieText9.setText(titleList.get(9));
    }

    @FXML
    private ArrayList<String> displayRatedByUser(int userID) throws IOException {
        ProfileID.setText(String.valueOf(userID));
        HashMap<Integer, String> titleMap = importTitles();
        HashMap<Integer, HashMap<Integer, Double>> seenBy = importSeenBy(0);
        HashMap<Integer, Double> seenByHash = seenBy.get(userID);
        LinkedHashMap<Integer, Double> seenByLinked = new LinkedHashMap<>(seenByHash);

        LinkedHashMap<Integer, Double> seenByUserMap = seenByLinked.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (x, y) -> x,
                        LinkedHashMap::new));

        List<Integer> ratingList = new ArrayList<>();
        List<String> seenByUser = new ArrayList<>();
        int i = 0;

        for (int movieID : seenByUserMap.keySet()) {

            if (i > 9) {
                break;
            }

            int rating = seenByUserMap.get(movieID).intValue();

            ratingList.add(rating);
            seenByUser.add(titleMap.get(movieID));

            i++;
        }

        Rating0.setText("Rated: " + String.valueOf(ratingList.get(0)));
        Rating1.setText("Rated: " + String.valueOf(ratingList.get(1)));
        Rating2.setText("Rated: " + String.valueOf(ratingList.get(2)));
        Rating3.setText("Rated: " + String.valueOf(ratingList.get(3)));
        Rating4.setText("Rated: " + String.valueOf(ratingList.get(4)));
        Rating5.setText("Rated: " + String.valueOf(ratingList.get(5)));
        Rating6.setText("Rated: " + String.valueOf(ratingList.get(6)));
        Rating7.setText("Rated: " + String.valueOf(ratingList.get(7)));
        Rating8.setText("Rated: " + String.valueOf(ratingList.get(8)));
        Rating9.setText("Rated: " + String.valueOf(ratingList.get(9)));

        MovieTitle0.setText(seenByUser.get(0));
        MovieTitle1.setText(seenByUser.get(1));
        MovieTitle2.setText(seenByUser.get(2));
        MovieTitle3.setText(seenByUser.get(3));
        MovieTitle4.setText(seenByUser.get(4));
        MovieTitle5.setText(seenByUser.get(5));
        MovieTitle6.setText(seenByUser.get(6));
        MovieTitle7.setText(seenByUser.get(7));
        MovieTitle8.setText(seenByUser.get(8));
        MovieTitle9.setText(seenByUser.get(9));

        return (ArrayList<String>) seenByUser;
    }

    private void displaySimilarUsers(int userID) {
        HashMap<Integer, HashMap<Integer, Double>> correlationsMap = importCorrelations(0, 100);
        HashMap<Integer, Double> similarUsersHash = correlationsMap.get(userID);
        LinkedHashMap<Integer, Double> similarUsersLinked = new LinkedHashMap<>(similarUsersHash);

        LinkedHashMap<Integer, Double> similarUsersSorted = similarUsersLinked.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (x, y) -> x,
                        LinkedHashMap::new));

        List<Integer> similarUsers = new ArrayList<>();
        List<Integer> correlations = new ArrayList<>();
        int i = 0;

        for (int user : similarUsersSorted.keySet()) {

            if (i > 9) {
                break;
            }

            int correlation = (int) (similarUsersSorted.get(user)*100);

            similarUsers.add(user);
            correlations.add(correlation);

            i++;
        }

        SimilarUser0.setText(String.valueOf(similarUsers.get(0)));
        SimilarUser1.setText(String.valueOf(similarUsers.get(1)));
        SimilarUser2.setText(String.valueOf(similarUsers.get(2)));
        SimilarUser3.setText(String.valueOf(similarUsers.get(3)));
        SimilarUser4.setText(String.valueOf(similarUsers.get(4)));
        SimilarUser5.setText(String.valueOf(similarUsers.get(5)));
        SimilarUser6.setText(String.valueOf(similarUsers.get(6)));
        SimilarUser7.setText(String.valueOf(similarUsers.get(7)));
        SimilarUser8.setText(String.valueOf(similarUsers.get(8)));
        SimilarUser9.setText(String.valueOf(similarUsers.get(9)));

        Correlation0.setText(String.valueOf(correlations.get(0)) + "%");
        Correlation1.setText(String.valueOf(correlations.get(1)) + "%");
        Correlation2.setText(String.valueOf(correlations.get(2)) + "%");
        Correlation3.setText(String.valueOf(correlations.get(3)) + "%");
        Correlation4.setText(String.valueOf(correlations.get(4)) + "%");
        Correlation5.setText(String.valueOf(correlations.get(5)) + "%");
        Correlation6.setText(String.valueOf(correlations.get(6)) + "%");
        Correlation7.setText(String.valueOf(correlations.get(7)) + "%");
        Correlation8.setText(String.valueOf(correlations.get(8)) + "%");
        Correlation9.setText(String.valueOf(correlations.get(9)) + "%");
    }
}