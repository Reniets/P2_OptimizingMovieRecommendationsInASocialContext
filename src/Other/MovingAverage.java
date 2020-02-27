package Other;

public class MovingAverage {
    private long totalEntries = 0;
    private double average = 0;

    public void add(double value){
        this.totalEntries++;
        this.average = this.average + ((1/ (double) totalEntries) * (value - this.average));
    }

    public void reset(){
        this.average = 0;
        this.totalEntries = 0;
    }

    public long getTotalEntries() {
        return totalEntries;
    }

    public double getAverage() {
        return average;
    }
}
