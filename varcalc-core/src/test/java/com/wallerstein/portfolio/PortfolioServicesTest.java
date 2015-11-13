package com.wallerstein.portfolio;

import com.wallerstein.model.ClosingPriceTS;
import com.wallerstein.model.Portfolio;
import com.wallerstein.model.Position;
import com.wallerstein.model.ReturnsTimeSeries;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PortfolioServicesTest {

    private Portfolio portfolio = null;
    private TimeSeries ts1 = null;
    private TimeSeries ts2 = null;
    private List<ClosingPriceTS> closingPrices = null;
    private PortfolioServices portfolioServices = null;

    @Before
    public void setUp() throws Exception {
        List<Position> positionList = Arrays.asList(
            new Position("XYZ", 100),
            new Position("ZZZ", 50));
        portfolio = new Portfolio(positionList);

        ts1 = new TimeSeries("XYZ");
        ts1.add(new Day(2, 1, 2011), 181.8);
        ts1.add(new Day(3, 1, 2011), 167.3);
        ts1.add(new Day(4, 1, 2011), 153.8);
        ts1.add(new Day(5, 1, 2011), 167.6);
        ts1.add(new Day(6, 1, 2011), 158.8);
        ts1.add(new Day(7, 1, 2011), 148.3);

        ts2 = new TimeSeries("ZZZ");
        ts2.add(new Day(2, 1, 2011), 141.8);
        ts2.add(new Day(3, 1, 2011), 147.3);
        ts2.add(new Day(4, 1, 2011), 143.8);
        ts2.add(new Day(5, 1, 2011), 147.6);
        ts2.add(new Day(6, 1, 2011), 148.8);
        ts2.add(new Day(7, 1, 2011), 158.3);

        closingPrices = Arrays.asList(
                new ClosingPriceTS("XYZ", ts1, 5),
                new ClosingPriceTS("ZZZ", ts2, 5)
        );

        portfolioServices = new PortfolioServices();
    }

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    @Test
    public void testCalculatePortfolioReturns() throws Exception {
        ReturnsTimeSeries returnsTS = portfolioServices.calculatePortfolioReturns(closingPrices, portfolio);

        double returns[] = {-0.046, -0.063, 0.070, -0.034, -0.025};

        assertEquals(returnsTS.getDataSet().getItemCount(), 5);
        for (int i = 0; i < returnsTS.getDataSet().getItemCount(); i++) {
            assertEquals(returns[i], returnsTS.getDataSet().getDataItem(i).getValue().doubleValue(), 0.01);
        }
    }

    @Test
    public void testBadTS() throws Exception {
        TimeSeries ts = new TimeSeries("Bogus");
        ts.add(new Day(2, 1, 2011), 141.8);

        closingPrices = Arrays.asList(
                new ClosingPriceTS("XYZ", ts1, 5),
                new ClosingPriceTS("Bogus", ts, 5));

        thrown.expect(IllegalArgumentException.class);
        portfolioServices.calculatePortfolioReturns(closingPrices, portfolio);
    }
}