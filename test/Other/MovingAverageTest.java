package Other;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MovingAverageTest {

    @Test
    void reset() {
        MovingAverage movingAverage = new MovingAverage();
        movingAverage.add(123);
        movingAverage.add(32131);

        movingAverage.reset();

        assertEquals(0, movingAverage.getAverage());
    }

    @Test
    void getTotalEntries() {
        MovingAverage movingAverage = new MovingAverage();
        movingAverage.add(123);
        movingAverage.add(32131);

        assertEquals(2, movingAverage.getTotalEntries());
    }

    @Test
    void getAverage() {
        MovingAverage movingAverage = new MovingAverage();
        movingAverage.add(10);
        movingAverage.add(30);
        movingAverage.add(20);

        assertEquals(20, movingAverage.getAverage(), 0.0001);
    }
}