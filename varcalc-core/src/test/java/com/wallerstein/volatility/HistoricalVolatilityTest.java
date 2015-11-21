package com.wallerstein.volatility;

import com.wallerstein.model.ClosingPriceTS;
import com.wallerstein.model.ReturnsTimeSeries;
import junit.framework.TestCase;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class HistoricalVolatilityTest extends TestCase {

    @Test
    public void testSingleStock() throws Exception {
        final String SYMBOL = "VZ";

        TimeSeries ts = new TimeSeries(SYMBOL);
        ts.add(new Day(2, 1, 2015), 46.4);
        ts.add(new Day(3, 1, 2015), 46.88);
        ts.add(new Day(4, 1, 2015), 46.78);
        ts.add(new Day(5, 1, 2015), 46.45);
        ts.add(new Day(6, 1, 2015), 46.14);
        ts.add(new Day(7, 1, 2015), 46.2);
        ts.add(new Day(8, 1, 2015), 45.78);
        ts.add(new Day(9, 1, 2015), 45.3);
        ts.add(new Day(10, 1, 2015), 45.1);

        List<ClosingPriceTS> cpts = Arrays.asList(new ClosingPriceTS(SYMBOL, ts, 1));

        assertEquals(
                0.580748772,
                new HistoricalVolCalculator().calculateDailyVolatility(cpts), 0.0001);
    }

    @Test
    public void testPortfolio() throws Exception {

        final int YEAR_INTERVAL = 5;

        TimeSeries ts1 = new TimeSeries("XYZ");
        ts1.add(new Day(2, 1, 2015), 181.8);
        ts1.add(new Day(3, 1, 2015), 167.3);
        ts1.add(new Day(4, 1, 2015), 153.8);
        ts1.add(new Day(5, 1, 2015), 167.6);
        ts1.add(new Day(6, 1, 2015), 158.8);
        ts1.add(new Day(7, 1, 2015), 148.3);

        ClosingPriceTS closingPriceTS1 = new ClosingPriceTS("XYZ", ts1, YEAR_INTERVAL);

        TimeSeries ts2 = new TimeSeries("ZZZ");
        ts2.add(new Day(2, 1, 2015), 141.8);
        ts2.add(new Day(3, 1, 2015), 147.3);
        ts2.add(new Day(4, 1, 2015), 143.8);
        ts2.add(new Day(5, 1, 2015), 147.6);
        ts2.add(new Day(6, 1, 2015), 148.8);
        ts2.add(new Day(7, 1, 2015), 158.3);

        ClosingPriceTS closingPriceTS2 = new ClosingPriceTS("ZZZ", ts2, YEAR_INTERVAL);

        List<ClosingPriceTS> cpts = Arrays.asList(closingPriceTS1, closingPriceTS2);

        assertEquals(
                12.07522717,
                new HistoricalVolCalculator().calculateDailyVolatility(cpts), 0.0001);
    }
}