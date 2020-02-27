package CSV;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CSV_ReaderTest {

    private static final ArrayList<ArrayList<String>> testFileData = new ArrayList<>(6);

    @BeforeAll
    static void beforeAll(){
        // Line 1
        ArrayList<String> line_1 = new ArrayList<>(3);
        line_1.add("1");
        line_1.add("2");
        line_1.add("3");

        // Line 2
        ArrayList<String> line_2 = new ArrayList<>(3);
        line_2.add("2");
        line_2.add("2");
        line_2.add("2");

        // Line 3
        ArrayList<String> line_3 = new ArrayList<>(3);
        line_3.add("3");
        line_3.add("3");
        line_3.add("3");

        // Line 4
        ArrayList<String> line_4 = new ArrayList<>(3);
        line_4.add("3");
        line_4.add("2");
        line_4.add("1");

        // Line 5
        ArrayList<String> line_5 = new ArrayList<>(3);
        line_5.add("5");
        line_5.add("8");
        line_5.add("7");

        // Line 6
        ArrayList<String> line_6 = new ArrayList<>(3);
        line_6.add("6");
        line_6.add("9");
        line_6.add("6");

        testFileData.add(line_1);
        testFileData.add(line_2);
        testFileData.add(line_3);
        testFileData.add(line_4);
        testFileData.add(line_5);
        testFileData.add(line_6);
    }

    @Test
    static void countLines01() {
        CSV_Reader testReader = new CSV_Reader("Tests/Data/CSV_TestFile.csv");

        int testResult = testReader.getTotalLines();
        int expected = testFileData.size();
        assertEquals(expected, testResult);
    }

    @Test
    void jumpToLine01() {
        CSV_Reader testReader = new CSV_Reader("Tests/Data/CSV_TestFile.csv");

        int lineNumber = 4; //Enter line to jump to - Must be in index

        boolean jumpSuccessful = testReader.jumpToLine(lineNumber);

        assertTrue(jumpSuccessful);
    }

    @Test
    void jumpToLine02() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            CSV_Reader testReader = new CSV_Reader("Tests/Data/CSV_TestFile.csv");

            int lineNumber = 20; //Enter line to jump to - Must be out of index

            testReader.jumpToLine(lineNumber);
        });
    }

    @Test
    void jumpToLineAndRead() {
        CSV_Reader testReader = new CSV_Reader("Tests/Data/CSV_TestFile.csv");

        int lineNumber = 5; //Enter line to jump to - Must be out of index
        boolean jumpSuccessful = testReader.jumpToLine(lineNumber);

        ArrayList<String> line_5 = testReader.nextLine();

        assertTrue(jumpSuccessful);
        assertArrayEquals(line_5.toArray(), testFileData.get(5).toArray());
    }

    @Test
    void nextLine01() {
        CSV_Reader testReader = new CSV_Reader("Tests/Data/CSV_TestFile.csv");

        int startingLine = 3;

        boolean jumpSuccessful = testReader.jumpToLine(startingLine);

        assertTrue(jumpSuccessful);

        ArrayList<String> extractedLines = testReader.nextLine();

        ArrayList<String> expectedLine = testFileData.get(3);

        assertArrayEquals(expectedLine.toArray(), extractedLines.toArray());
    }

    @Test
    void getFilePath01(){
        String expectedFilePath = "Tests/Data/CSV_TestFile.csv";

        CSV_Reader testReader = new CSV_Reader(expectedFilePath);

        assertTrue(expectedFilePath.equals(testReader.getFilePath()));
    }

    @Test
    void getCurrentLine01(){
        CSV_Reader testReader = new CSV_Reader("Tests/Data/CSV_TestFile.csv");

        int startingLine = 3;

        int expectedLine = 4;

        assertTrue(testReader.jumpToLine(startingLine));

        testReader.nextLine();

        assertTrue(expectedLine == testReader.getCurrentLine());
    }

}