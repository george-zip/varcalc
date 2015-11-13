package com.wallerstein.model;

import junit.framework.TestCase;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.junit.Before;

public class ClosingPriceTSTest extends TestCase {

    private static final int YEAR_INTERVAL = 5;
    private TimeSeries ts = null;
    ClosingPriceTS closingPriceTS = null;

    @Before
    public void setUp() throws Exception {
        this.ts = new TimeSeries("XYZ");
        ts.add(new Day(2, 1, 2011), 181.8);
        ts.add(new Day(3, 1, 2011), 167.3);
        ts.add(new Day(4, 1, 2011), 153.8);
        ts.add(new Day(5, 1, 2011), 167.6);
        ts.add(new Day(6, 1, 2011), 158.8);
        ts.add(new Day(7, 1, 2011), 148.3);
        closingPriceTS = new ClosingPriceTS("XYZ", ts, YEAR_INTERVAL);
    }

    public void testGetSymbol() throws Exception {
        assertEquals(closingPriceTS.getSymbol(), "XYZ");
    }

    public void testGetDataSet() throws Exception {
        assertEquals(closingPriceTS.getDataSet(), ts);
    }

    public void testGetYearInterval() throws Exception {
        assertEquals(closingPriceTS.getYearInterval(), YEAR_INTERVAL);
    }

    public void testGetClosePrice() throws Exception {
        assertEquals(closingPriceTS.getPreviousClose(), 148.3, 0.01);
    }
}