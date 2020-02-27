package CSV;

import java.io.*;
import java.util.ArrayList;

public class CSV_Writer {

    /**
     *
     * Local fields
     *
     */

    private File file;
    private BufferedWriter bufferedWriter;

    /**
     *
     * Constructor & "constructor methods"
     *
     */

    // Constructor
    public CSV_Writer() {
    }

    public CSV_Writer(File file, boolean append) throws IOException {
        this();
        this.targetFile(file, append);
    }

    public void targetFile(File file, boolean append) throws IOException {
        this.targetFilePrivate(file, append);
    }

    // Method to target new file and get a fileWriter
    private void targetFilePrivate(File file, boolean append) throws IOException {
        if (this.file != null){
            this.close();
        }

        this.file = file;

        this.bufferedWriter = newBufferedReader(file, append);

        // Print new line if append mode is true.
        if (append){
            this.bufferedWriter.write("\n");
        }
    }

    private BufferedWriter newBufferedReader(File file, boolean append) throws IOException {
        OutputStream outputStream = new FileOutputStream(file, append);
        return new BufferedWriter(new OutputStreamWriter(outputStream, "ISO-8859-1"));
    }

    /**
     *
     * Getters
     *
     */

    public BufferedWriter getBufferedWriter() {
        return bufferedWriter;
    }

    File getFile() {
        return file;
    }

    /**
     *
     * Print methods
     *
     */

    // Print
    public void print(ArrayList<String> line) throws IOException {
        int totalElements = line.size();
        int elementID = 0;

        if (this.bufferedWriter != null){
            for (String element : line){
                this.bufferedWriter.write(element);

                if (elementID != totalElements - 1){
                    this.bufferedWriter.write(",");
                }

                elementID++;
            }
        } else {
            throw new IOException("The CSV Writer is not targeting any file, or has recently been closed");
        }
    }

    // println
    public void println(ArrayList<String> line) throws IOException {
        this.print(line);
        this.bufferedWriter.write("\n");
    }

    /**
     *
     * Functional methods
     *
     */

    // Close the printWriter
    public void close() throws IOException {
        this.file = null;

        this.bufferedWriter.flush();
        this.bufferedWriter.close();

        this.bufferedWriter = null;
    }
}
