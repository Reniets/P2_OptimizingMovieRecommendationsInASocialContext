package CSV;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class CSV_Reader implements Iterable<ArrayList<String>> {

    private String filePath;
    private int currentLine;
    private int totalLines;
    private boolean attachedToCSV;

    private File file;
    private BufferedReader b;

    public CSV_Reader() {
        this.attachedToCSV = false;
        this.currentLine = 0;
    }

    public CSV_Reader(String filePath) {
        this();

        this.targetFile(filePath);
    }

    public int getCurrentLine() {
        return this.currentLine;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public int getTotalLines() {
        if (attachedToCSV) {
            return this.totalLines;
        } else {
            return -1;
        }
    }

    public void targetFile(String filePath) {
        this.filePath = filePath;

        try {

            if (this.attachedToCSV){
                this.close();
            }

            // Prepare reading from file
            this.currentLine = 0;
            this.file = new File(filePath);
            this.b = new BufferedReader(new FileReader(file));
            this.attachedToCSV = true;
            this.totalLines = countLines();

            // If an error occurred, catch it and print the error.
        } catch (IOException e) {
            this.attachedToCSV = false;
            e.printStackTrace();
        }
    }

    public ArrayList<String> nextLine() {
        String readLine = "";

        if (this.attachedToCSV) {

            try {

                if (this.currentLine == this.totalLines) {
                    this.b.close();
                    this.b = new BufferedReader(new FileReader(file));
                    this.currentLine = 0;
                }

                readLine = this.b.readLine();
                this.currentLine++;

            } catch (IOException e) {
                e.printStackTrace();
            }

            String[] output = readLine.split(",");
            return new ArrayList<>(Arrays.asList(output));

        } else {
            throw new NullPointerException("CSVR_Reader was not connected to any file");
        }
    }

    public boolean jumpToLine(int line) {

        while (this.totalLines >= line && this.currentLine != line) {
            this.nextLine();
        }

        if (this.currentLine == line) {
            return true;
        } else {
            throw new IllegalArgumentException("Line not found. The file has a total of " + this.totalLines + " lines, and you wanted to jump to line " + line + ".");
        }
    }

    private int countLines() {

        if (attachedToCSV) {
            String readLine;
            int count = 0;
            try {
                BufferedReader b = new BufferedReader(new FileReader(this.filePath));
                while ((readLine = b.readLine()) != null) {
                    count++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return count;

        } else {
            return -1;
        }
    }

    public void close() throws IOException {
        b.close();
        this.attachedToCSV = false;
        this.currentLine = 0;
        this.totalLines = -1;
    }

    public void printPercentage(){
        if (this.getCurrentLine() != 0 && this.totalLines >= 100 && this.getCurrentLine() % (this.getTotalLines()/100) == 0){
            System.out.println(Math.ceil( ((double) this.getCurrentLine() / (double) this.getTotalLines()) * 100 )  + "%");
        }
    }

    @Override
    public Iterator<ArrayList<String>> iterator() {
        return new CSV_Reader_Iterator(this);
    }
}
