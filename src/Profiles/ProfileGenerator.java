package Profiles;

import CSV.CSV_Reader;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

public class ProfileGenerator {

    public LinkedHashMap<Integer, MovieProfile> generateListMP(String moviesSRC, String genomeScoresSRC) {
        CSV_Reader CSVR = new CSV_Reader(moviesSRC);

        LinkedHashMap<Integer, MovieProfile> MP = new LinkedHashMap<>();

        for (ArrayList<String> readLine : CSVR) {

            MovieProfile tempMP = this.extractMovieMP(readLine, genomeScoresSRC);
            int tempID = tempMP.getID();

            MP.put(tempID, tempMP);

            CSVR.printPercentage();
        }

        return MP;
    }

    private MovieProfile extractMovieMP(ArrayList<String> movieLine, String genomeScoresSRC) {
        // Get movie ID as int
        int ID = Integer.parseInt(movieLine.get(0));

        // Split genres into a string array
        String[] genres = movieLine.get(movieLine.size() - 1).split("\\|");

        // Init CSV objects
        CSV_Reader CSVR = new CSV_Reader(genomeScoresSRC);

        ArrayList<String> readLine;
        double[] genomeScores = new double[34];

        int lineCheck;
        boolean reachedEndOfFile;

        // Spring til det rigtige sted i dataen
        do {
            readLine = CSVR.nextLine();
            lineCheck = CSVR.getCurrentLine();
            reachedEndOfFile = lineCheck == CSVR.getTotalLines();
        } while (!readLine.get(0).equals(movieLine.get(0)) && !reachedEndOfFile);

        // Indlæs data
        for (int i = 0; i < 34; i++) {
            if (reachedEndOfFile) {
                genomeScores[i] = 0.5;
            } else {
                genomeScores[i] = Double.parseDouble(readLine.get(2));
            }
            readLine = CSVR.nextLine();
        }

        // Generate movie profile
        return new MovieProfile(ID, genomeScores, genres);
    }

    public LinkedHashMap<Integer, UserProfile> generateListUP(LinkedHashMap<Integer, MovieProfile> MP, String userRatingSRC) {
        int totalGenomeTags = MP.get(1).genomeRatings.length;
        int totalGenres = MP.get(1).genres.length;

        CSV_Reader CSVR = new CSV_Reader(userRatingSRC);

        LinkedHashMap<Integer, UserProfile> linkedHashMapUP = new LinkedHashMap<>();

        // For every line in ratings.csv
        for (ArrayList<String> readLine : CSVR) {

            // extract data from line
            int userID = Integer.parseInt(readLine.get(0));
            int movieID = Integer.parseInt(readLine.get(1));
            double rating = Double.parseDouble(readLine.get(2)) / 5;

            // If a user with "userID" dont exist, then add one
            if ( !(linkedHashMapUP.containsKey(userID)) ) {
                linkedHashMapUP.put(userID, new UserProfile(userID, totalGenomeTags, totalGenres));
            }

            // Update the users ratings
            linkedHashMapUP.get(userID).updateRatings(MP.get(movieID), rating);

            CSVR.printPercentage();
        }

        return linkedHashMapUP;
    }

    public <T extends Profile> void exportProfileList(String newFilePath, LinkedHashMap<Integer, T> profileList) {
        try {

            // Prepare printWriter to write in specified file.
            FileWriter fileWriter = new FileWriter(newFilePath, false);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            for (T profile : profileList.values()) {

                // Print ID
                printWriter.print(profile.getID() + ",");

                // Print genome ratings
                exportGenomeToFile(printWriter, profile);

                // Print genres
                exportGenreToFile(printWriter, profile);
            }

            printWriter.close();

            // If an error occurred, catch it and print the error.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exportGenomeToFile(PrintWriter printWriter, Profile obj) {
        for (int g = 0; g < obj.getGenomeRatings().length; g++) {
            if (g != obj.getGenomeRatings().length - 1) {
                printWriter.print(obj.getGenomeRatings()[g] + "|");
            } else {
                printWriter.print(obj.getGenomeRatings()[g]);
            }
        }

        printWriter.print(",");
    }

    private void exportGenreToFile(PrintWriter printWriter, Profile obj) {

        for (int g = 0; g < obj.getGenres().length; g++) {
            printWriter.print(obj.getGenres()[g]);

            if (g != obj.getGenres().length - 1) {
                printWriter.print("|");
            }
        }

        printWriter.println();
    }
}
