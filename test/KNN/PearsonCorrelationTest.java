package KNN;

import Other.DataImporter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PearsonCorrelationTest {
    private PearsonCorrelation pearsonCorrelation;

    @BeforeEach
    void PearsonCorrelation() {
        pearsonCorrelation = new PearsonCorrelation(5);
    }

    @Test
    void getSeenBy01() {
        assertEquals(DataImporter.importSeenBy(5), pearsonCorrelation.getSeenBy());
    }

    // Check that the from user to himself has correlation 1
    @Test
    void calcCorrelation01() {
        assertEquals(1, pearsonCorrelation.calcCorrelation(1, 1));
    }

    // Check that two opposite users have a correlation of -1
    @Test
    void calcCorrelation02() {
        assertEquals(-1, pearsonCorrelation.calcCorrelation(6, 7));
    }

    // Check that two users with less than 7 commonly rated movies have no correlation
    @Test
    void calcCorrelation03() {
        assertEquals(0, pearsonCorrelation.calcCorrelation(1, 3));
    }

    // Check that a user with a length of 0 returns the average of the average rating between the two users
    @Test
    void calcCorrelation04() {
        assertEquals(0, pearsonCorrelation.calcCorrelation(5, 8));
    }

    // Check that there is symmetry in the calculation of correlations
    @Test
    void calcCorrelation05() {
        assertEquals(pearsonCorrelation.calcCorrelation(1, 2), pearsonCorrelation.calcCorrelation(2, 1));
    }

    // Check that a user who only rates 1 also has an averageOverallRating of 1
    @Test
    void averageOverallRating01() {
        assertEquals(1, pearsonCorrelation.averageOverallRating(8));
    }

    // Check that the correct number of neighbors are delimited and exported to the file
    @Test
    void exportCorrelations01() {
        try {
            pearsonCorrelation.exportCorrelations(2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Read from generated file and assert that it is correct
        Path filePath = Paths.get("data/sep/5/pearsonCorrelations/pearsonCorrelations_3_5.csv");

        try (BufferedReader br = Files.newBufferedReader(filePath)) {
            assertEquals("1,2|0.000,3|0.000,4|0.000", br.readLine());
            assertEquals("2,1|0.000,3|0.000,4|0.000", br.readLine());
            assertEquals("3,1|0.000,2|0.000,4|0.000", br.readLine());
            assertEquals("4,1|0.000,2|0.000,3|0.000", br.readLine());
            assertEquals("5,6|1.000,1|0.000,2|0.000", br.readLine());
            assertEquals("6,5|1.000,1|0.000,2|0.000", br.readLine());
            assertEquals("7,1|0.000,2|0.000,3|0.000", br.readLine());
            assertEquals("8,1|0.000,2|0.000,3|0.000", br.readLine());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Checks that neighbors are delimited and sorted
    @Test
    void delimit_KNearestNeighbors01() {
        HashMap<Integer, Double> allNearestNeighbors = new HashMap<>();
        allNearestNeighbors.put(1, 1.0);
        allNearestNeighbors.put(2, 2.0);
        allNearestNeighbors.put(3, 3.0);
        allNearestNeighbors.put(4, 4.0);

        LinkedHashMap<Integer, Double> expected = new LinkedHashMap<>();
        expected.put(4, 4.0);
        expected.put(3, 3.0);

        assertEquals(expected, pearsonCorrelation.delimit_kNearestNeighbors(allNearestNeighbors, 2));
    }
}