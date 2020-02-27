package KNN;

import Profiles.MovieProfile;
import Profiles.Profile;
import Profiles.UserProfile;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EuclideanDistanceTest {
    @Test
    void findNearestNeighbors01() {
        UserProfile userProfile1 = new UserProfile(1, 2, 1);
        UserProfile userProfile2 = new UserProfile(2, 2, 1);
        UserProfile userProfile3 = new UserProfile(3, 2, 1);


        double[] genomeRatings = {1.0, 2.0};
        String[] genres =  {"Action"};
        MovieProfile movieProfile1 = new MovieProfile(1, genomeRatings, genres);
        MovieProfile movieProfile2 = new MovieProfile(2, genomeRatings, genres);


        userProfile1.updateRatings(movieProfile1, 5.0);
        userProfile1.updateRatings(movieProfile2, 5.0);
        userProfile2.updateRatings(movieProfile1, 5.0);
        userProfile2.updateRatings(movieProfile2, 5.0);
        userProfile3.updateRatings(movieProfile1, 5.0);
        userProfile3.updateRatings(movieProfile2, 5.0);

        HashMap<Integer, Profile> userProfileHashMap = new HashMap<>();
        userProfileHashMap.put(1, userProfile1);
        userProfileHashMap.put(2, userProfile2);
        userProfileHashMap.put(3, userProfile3);

        EuclideanDistance euclideanDistance = new EuclideanDistance();

        try {
            euclideanDistance.findNearestNeighbors(userProfileHashMap, 2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Path filePath = Paths.get("data/euclideanNearestNeighbors.csv");

        try (BufferedReader br = Files.newBufferedReader(filePath)) {
            assertEquals("1,2|1.0000,3|1.0000", br.readLine());
            assertEquals("2,1|1.0000,3|1.0000", br.readLine());
            assertEquals("3,1|1.0000,2|1.0000", br.readLine());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
