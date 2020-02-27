package Profiles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserProfileTest {

    private static final double[] genomeRatings = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
    private static final double[] expectedGenres = {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private static final String[] genres = {"Action"};
    private static final MovieProfile movieProfile = new MovieProfile(0, genomeRatings, genres);

    @Test
    void userProfile(){
        UserProfile userProfile = new UserProfile(0, genomeRatings.length, expectedGenres.length);

        userProfile.updateRatings(movieProfile, 0.6);
        userProfile.updateRatings(movieProfile, 0.6);

        for (double value : userProfile.getGenomeRatings()) {
            assertEquals(0, value, 0.001);
        }

        for (double value : userProfile.getGenres()) {
            assertEquals(0, value, 0.001);
        }
    }
}