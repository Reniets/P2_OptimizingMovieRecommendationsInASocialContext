package Other;

import CSV.CSV_Reader;
import Profiles.Profile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class DataImporter {
    public static HashSet<Integer> importMovieIDS() {
        CSV_Reader CSVR = new CSV_Reader("data/cleaned/movieIDS.csv");

        HashSet<Integer> movieIDS = new HashSet<>();

        for (ArrayList<String> readMovieID : CSVR) {
            movieIDS.add(Integer.parseInt(readMovieID.get(0)));
        }

        return movieIDS;
    }

    public static HashMap<Integer, HashMap<Integer, Double>> importSeenBy(int fileNumber) {
        CSV_Reader CSVR = new CSV_Reader("data/sep/" + fileNumber + "/seenBy_" + fileNumber + ".csv");

        HashMap<Integer, HashMap<Integer, Double>> seenBy = new HashMap<>();

        for (ArrayList<String> readLine : CSVR) {
            int mainUserID = Integer.parseInt(readLine.get(0));

            for (int i = 1; i < readLine.size(); i++) {
                String movie = readLine.get(i);
                String[] movieSplit = movie.split("\\|");
                int movieID = Integer.parseInt(movieSplit[0]);
                double rating = Double.parseDouble(movieSplit[1]);

                if (!seenBy.containsKey(mainUserID)) {
                    seenBy.put(mainUserID, new HashMap<>());
                }

                seenBy.get(mainUserID).put(movieID, rating);
            }
        }

        return seenBy;
    }

    public static HashMap<Integer, HashMap<Integer, Double>> importCorrelations(int fileNumber) {
        String filePath = "data/sep/" + fileNumber + "/pearsonCorrelations/pearsonCorrelations_6039" + "_" + fileNumber + ".csv";

        return importCorrelations(filePath);
    }

    public static HashMap<Integer, HashMap<Integer, Double>> importCorrelations(int fileNumber, int k) {
        String filePath = "data/sep/" + fileNumber + "/pearsonCorrelations/pearsonCorrelations_" + k + "_" + fileNumber + ".csv";

        return importCorrelations(filePath);
    }

    private static HashMap<Integer, HashMap<Integer, Double>> importCorrelations(String filePath) {
        CSV_Reader CSVR = new CSV_Reader(filePath);

        HashMap<Integer, HashMap<Integer, Double>> KNN = new HashMap<>();

        for (ArrayList<String> readLine : CSVR) {
            int mainUserID = Integer.parseInt(readLine.get(0));

            for (int i = 1; i < readLine.size(); i++) {
                String movieRatings = readLine.get(i);
                String[] movieRatingSplit = movieRatings.split("\\|");
                int subUserID = Integer.parseInt(movieRatingSplit[0]);
                double rating = Double.parseDouble(movieRatingSplit[1]);

                if (!KNN.containsKey(mainUserID)) {
                    KNN.put(mainUserID, new HashMap<>());
                }

                KNN.get(mainUserID).put(subUserID, rating);
            }
        }


        return KNN;
    }

    public static HashMap<Integer, Double> importAvgOverallRatings(int fileNumber) {
        CSV_Reader CSVR = new CSV_Reader("data/sep/" + fileNumber + "/avgOverallRatings_" + fileNumber + ".csv");

        HashMap<Integer, Double> avgRatings = new HashMap<>();

        for (ArrayList<String> readLine : CSVR) {
            String line = readLine.get(0);

            String[] avgRatingSplit = line.split("\\|");
            int userID = Integer.parseInt(avgRatingSplit[0]);
            double avgRating = Double.parseDouble(avgRatingSplit[1]);

            avgRatings.put(userID, avgRating);
        }

        return avgRatings;
    }

    public static HashMap<Integer, Profile> importMovieProfileList(int fileNumberInt) {
        String fileNumberString = Integer.toString(fileNumberInt);
        String path = "data/sep/" + fileNumberString + "/movieProfiles.csv";

        return importProfileList(path);
    }

    public static HashMap<Integer, Profile> importUserProfileList(int fileNumberInt) {
        String fileNumberString = Integer.toString(fileNumberInt);
        String path = "data/sep/" + fileNumberString + "/userProfilesNegative_" + fileNumberString + ".csv";

        return importProfileList(path);
    }

    private static HashMap<Integer, Profile> importProfileList(String path) {
        CSV_Reader CSVR = new CSV_Reader(path);

        HashMap<Integer, Profile> profileHashMap = new HashMap<>();

        for (ArrayList<String> readLine : CSVR) {
            Profile tempProfile = new Profile(-1);
            tempProfile.importProfile(readLine);

            profileHashMap.put(tempProfile.getID(), tempProfile);
        }

        return profileHashMap;
    }

    public static HashMap<Integer, String> importTitles() throws IOException {
        CSV_Reader CSVR = new CSV_Reader("Data/Cleaned/movies.csv");
        HashMap<Integer, String> movieTitleHashMap = new HashMap<>();

        for (ArrayList<String> readLine : CSVR) {
            movieTitleHashMap.put(Integer.parseInt(readLine.get(0)), readLine.get(1));
        }

        CSVR.close();

        return movieTitleHashMap;
    }

    public static LinkedHashMap<Integer, Double> importRatings(int userID) throws IOException {
        CSV_Reader CSVR = new CSV_Reader("Data/allPredictions/0/" + userID + ".csv");
        LinkedHashMap<Integer, Double> movieRatingHashMap = new LinkedHashMap<>();

        ArrayList<String> readLine = CSVR.nextLine();

        for (String movieData : readLine) {
            String[] movieDataSplit = movieData.split("\\|");
            movieRatingHashMap.put(Integer.parseInt(movieDataSplit[0]), Double.parseDouble(movieDataSplit[1]));
        }

        CSVR.close();

        LinkedHashMap<Integer, Double> sortedMovieRatingHashMap = movieRatingHashMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (x, y) -> x,
                        LinkedHashMap::new));

        return sortedMovieRatingHashMap;
    }
}
