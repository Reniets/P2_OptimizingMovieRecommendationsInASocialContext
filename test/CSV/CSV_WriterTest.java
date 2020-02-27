package CSV;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CSV_WriterTest {

    private static final String filePath = "Tests/CSV/testData/CSVW.csv";
    private static final File file = new File(filePath);
    private static CSV_Writer CSVW;
    private static final ArrayList<String> thingsToPrint = new ArrayList<>(5);

    @BeforeAll
    static void beforeAll() {
        thingsToPrint.add("Thing 1");
        thingsToPrint.add("Thing b");
        thingsToPrint.add("Thing c");
        thingsToPrint.add("Thing d");
        thingsToPrint.add("Thing e");
    }

    @BeforeEach
    void beforeEach() throws IOException {
        CSVW = new CSV_Writer(file, false);
        CSVW.targetFile(file, false);
    }

    @Test
    void targetFile01() throws IOException {
        CSVW.targetFile(file, false);

        assertTrue(CSVW.getFile() != null);
        assertTrue(CSVW.getFile() == file);

        assertTrue(CSVW.getBufferedWriter() != null);
    }

    @Test
    void targetFile02() {
        File Dir = new File("Tests/CSV/testData");  // Directory

        Assertions.assertThrows(IOException.class, () -> {
            CSVW.targetFile(Dir, false);
        });
    }

    @Test
    void print01() throws IOException {
        CSVW.print(thingsToPrint);
        CSVW.close();   // If the writer is not closed, you cannot expect the file to be complete.

        CSV_Reader CSVR = new CSV_Reader(filePath);

        assertEquals(1, CSVR.getTotalLines());
        CSVR.close();
    }

    @Test
    void print02() throws IOException {
        CSVW.print(thingsToPrint);
        CSVW.close();   // If the writer is not closed, you cannot expect the file to be complete.

        CSV_Reader CSVR = new CSV_Reader(filePath);

        assertArrayEquals(thingsToPrint.toArray(), CSVR.nextLine().toArray());
        CSVR.close();
    }

    @Test
    void print03() throws IOException {
        CSVW.print(thingsToPrint);
        CSVW.print(thingsToPrint);
        CSVW.close();   // If the writer is not closed, you cannot expect the file to be complete.

        CSV_Reader CSVR = new CSV_Reader(filePath);

        assertEquals(1, CSVR.getTotalLines());
        CSVR.close();
    }

    @Test
    void println01() throws IOException {
        CSVW.println(thingsToPrint);
        CSVW.close();   // If the writer is not closed, you cannot expect the file to be complete.

        CSV_Reader CSVR = new CSV_Reader(filePath);
        assertEquals(1, CSVR.getTotalLines());
        CSVR.close();
    }

    @Test
    void println02() throws IOException {
        CSVW.println(thingsToPrint);
        CSVW.close();   // If the writer is not closed, you cannot expect the file to be complete.

        CSV_Reader CSVR = new CSV_Reader(filePath);

        assertArrayEquals(thingsToPrint.toArray(), CSVR.nextLine().toArray());
        CSVR.close();
    }

    @Test
    void println03() throws IOException {
        CSVW.println(thingsToPrint);
        CSVW.println(thingsToPrint);
        CSVW.close();   // If the writer is not closed, you cannot expect the file to be complete.

        CSV_Reader CSVR = new CSV_Reader(filePath);

        assertEquals(2, CSVR.getTotalLines());
        CSVR.close();
    }

    @Test
    void close() throws IOException {
        CSVW.close();

        assertTrue(CSVW.getFile() == null);
        assertTrue(CSVW.getBufferedWriter() == null);

        Assertions.assertThrows(IOException.class, () -> {
            CSVW.print(thingsToPrint);
        });
    }
}