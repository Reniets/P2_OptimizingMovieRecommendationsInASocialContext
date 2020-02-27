package CSV;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class CSV_ManipulatorTest {

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
    void extractUniqueCSV01() {
        HashSet<String> extractedLines;

        CSV_Manipulator testExtracter = new CSV_Manipulator();

        boolean testTrue = false;
        int column = 0;

        extractedLines = testExtracter.extractUnique(column, "Tests/Data/CSV_TestFile.csv");

        for(ArrayList<String> line : testFileData){
            testTrue = extractedLines.contains(line.get(column));
        }

        assertTrue(testTrue);
    }

    @Test
    void extractAll01(){
        ArrayList<String> extractedLines;

        CSV_Manipulator testExtracter = new CSV_Manipulator();
        String filePath = "Tests/Data/CSV_TestFile.csv";

        int column = 0;
        boolean testTrue = false;

        extractedLines = testExtracter.extractAll(column, filePath);

        for(int i = 0; i < extractedLines.size(); i++){
            testTrue = testFileData.get(i).get(column).equals(extractedLines.get(i));
        }

        assertTrue(testTrue);
    }


    @Test
    void CleanUp01(){
        ArrayList<String> extractedLines;
        ArrayList<HashSet<String>> expectedLines = new ArrayList<>(1);

        CSV_Manipulator testExtracter = new CSV_Manipulator();
        String filePathExtract = "Tests/Data/testExtract.csv";

        CSV_Manipulator testCleanup = new CSV_Manipulator();
        String filePathDirty = "Tests/Data/testDirty.csv";
        String filePathClean = "Tests/Data/testDirty_CLEANED.csv";

        int column = 0;
        boolean testTrue = false;

        ArrayList<Integer> columnArray = new ArrayList<>();
        columnArray.add(column);

        expectedLines.add(testExtracter.extractUnique(column, filePathExtract));

        testCleanup.cleanUp(columnArray, expectedLines, filePathDirty);

        extractedLines = testExtracter.extractAll(column, filePathClean);

        for (String extractedLine : extractedLines) {
            testTrue = testCleanup.existInArray(extractedLine, expectedLines.get(0));
        }

        assertTrue(testTrue);
    }
}