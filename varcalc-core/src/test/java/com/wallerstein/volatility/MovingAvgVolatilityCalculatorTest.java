package com.wallerstein.volatility;

import com.wallerstein.model.ReturnsTimeSeries;
import junit.framework.TestCase;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;

public class MovingAvgVolatilityCalculatorTest extends TestCase {

    public void testCalculateVolatility() throws Exception {
        final String SYMBOL = "VZ";

        TimeSeries ts = new TimeSeries(SYMBOL);
        ts.add(new Day(10, 11, 2015), 45.1);
        ts.add(new Day(9, 11, 2015), 45.3);
        ts.add(new Day(6, 11, 2015), 45.78);
        ts.add(new Day(5, 11, 2015), 46.2);
        ts.add(new Day(4, 11, 2015), 46.14);
        ts.add(new Day(3, 11, 2015), 46.45);
        ts.add(new Day(2, 11, 2015), 46.78);
        ts.add(new Day(30, 10, 2015), 46.88);
        ts.add(new Day(29, 10, 2015), 46.4);
        ts.add(new Day(28, 10, 2015), 46.48);

        assertEquals(
                0.006865,
                new MovingAvgVolatilityCalculator().calculateDailyVolatility(ReturnsTimeSeries.fromCPTS(ts)), 0.0001);
    }
}