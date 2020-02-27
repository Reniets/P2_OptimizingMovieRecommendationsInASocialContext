package Profiles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MovieProfileTest {

    private static final double[] genomeRatings = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
    private static final double[] expectedGenres = {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private static final String[] genres = {"Action"};

    @Test
    void verify() {
        assertEquals(36, genomeRatings.length);
        assertEquals(18, expectedGenres.length);
    }

    @Test
    void movieProfile(){
        MovieProfile movieProfile = new MovieProfile(0, genomeRatings, genres);

        assertEquals(0, movieProfile.ID);
        assertArrayEquals(expectedGenres, movieProfile.getGenres());
    }

}