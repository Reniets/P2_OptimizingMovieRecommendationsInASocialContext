package CSV;

import java.util.ArrayList;
import java.util.Iterator;

public class CSV_Reader_Iterator implements Iterator<ArrayList<String>>{
    private final CSV_Reader CSVR;
    private final int totalLines;
    private int currentLine = 0;

    public CSV_Reader_Iterator(CSV_Reader CSVR) {
        this.CSVR = CSVR;
        this.totalLines = CSVR.getTotalLines();

        this.CSVR.jumpToLine(0);
    }

    @Override
    public boolean hasNext() {
        return this.currentLine != this.totalLines;
    }

    @Override
    public ArrayList<String> next() {
        this.currentLine++;
        return this.CSVR.nextLine();
    }
}
