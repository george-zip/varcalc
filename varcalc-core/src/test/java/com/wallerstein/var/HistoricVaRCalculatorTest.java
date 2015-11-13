package com.wallerstein.var;

import com.wallerstein.model.ClosingPriceTS;
import com.wallerstein.model.Portfolio;
import com.wallerstein.model.Position;
import com.wallerstein.portfolio.PortfolioServices;
import com.wallerstein.timeseries.HistoricalClosingPrices;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HistoricVaRCalculatorTest {

    private static final int YEAR_INTERVAL = 5;
    private static final int ONE_DAY = 1;
    private static final int ONE_YEAR = 250;
    private static final double CONFIDENCE = 0.95;
    private static final String SYMBOL = "VZ";

    @Test
    public void testCalculateSingleStock() throws Exception {


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

        ClosingPriceTS closingPriceTS = new ClosingPriceTS(SYMBOL, ts, YEAR_INTERVAL);

        HistoricalClosingPrices closingPricesSource = mock(HistoricalClosingPrices.class);
        when(closingPricesSource.getClosingPricesForSymbol(anyString(), anyInt())).thenReturn(closingPriceTS);

        List<ClosingPriceTS> closingPriceTSList = Arrays.asList(closingPriceTS);

        assertEquals(45.10, closingPriceTS.getPreviousClose(), 0.01);

        when(closingPricesSource.getClosingPricesForPortfolio(any(Portfolio.class))).thenReturn(closingPriceTSList);

        PortfolioServices portfolioServices = new PortfolioServices();

        HistoricVaRCalculator historicVaRCalculator = new HistoricVaRCalculator
                (closingPricesSource, portfolioServices);

        List<Position> positionList = Arrays.asList(new Position(SYMBOL, 100));
        Portfolio portfolio = new Portfolio(positionList);

        assertEquals(4510.00, portfolio.getNMV(closingPriceTSList), 0.01);

        assertEquals(-44.36, historicVaRCalculator.calculateWorstLoss(portfolio, CONFIDENCE, ONE_DAY), 0.01);
        assertEquals(-701.43, historicVaRCalculator.calculateWorstLoss(portfolio, CONFIDENCE, ONE_YEAR), 0.01);
    }

    @Test
    public void testEmptyPortfolio() throws Exception {
        HistoricalClosingPrices closingPricesSource = mock(HistoricalClosingPrices.class);
        PortfolioServices portfolioServices = new PortfolioServices();
        HistoricVaRCalculator historicVaRCalculator = new HistoricVaRCalculator
                (closingPricesSource, portfolioServices);
        assertEquals(0.0, historicVaRCalculator.calculateWorstLoss
                (new Portfolio(new ArrayList<Position>()), CONFIDENCE, ONE_YEAR), 0.01);
    }

    @Test
    public void testBadClosingPriceSource() throws Exception {
        HistoricalClosingPrices closingPricesSource = mock(HistoricalClosingPrices.class);
        List<ClosingPriceTS> empty = new ArrayList<>();
        when(closingPricesSource.getClosingPricesForPortfolio(any(Portfolio.class))).thenReturn(empty);
        PortfolioServices portfolioServices = new PortfolioServices();
        HistoricVaRCalculator historicVaRCalculator = new HistoricVaRCalculator
                (closingPricesSource, portfolioServices);
        List<Position> positionList = Arrays.asList(new Position(SYMBOL, 100));
        Portfolio portfolio = new Portfolio(positionList);
        assertEquals(0.0, historicVaRCalculator.calculateWorstLoss
                (portfolio, CONFIDENCE, ONE_YEAR), 0.01);
    }
}