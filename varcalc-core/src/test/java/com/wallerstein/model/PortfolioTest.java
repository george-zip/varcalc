package com.wallerstein.model;

import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PortfolioTest {

    private TimeSeries ts1 = null;
    private TimeSeries ts2 = null;
    private List<ClosingPriceTS> closingPrices = null;

    private static final int YEAR_INTERVAL = 5;


    @Before
    public void setUp() throws Exception {
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
                new ClosingPriceTS("XYZ", ts1, YEAR_INTERVAL),
                new ClosingPriceTS("ZZZ", ts2, YEAR_INTERVAL));
    }

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    @Test
    public void testGetContainedPosition() throws Exception {
        Position position = new Position("IBM", 100.0);
        Iterator<Position> mockIterator = mock(Iterator.class);
        List<Position> mockList = mock(List.class);

        when(mockIterator.hasNext()).thenReturn(true, false);
        when(mockIterator.next()).thenReturn(position);
        when(mockList.iterator()).thenReturn(mockIterator);

        Portfolio portfolio = new Portfolio(mockList);
        assertEquals(portfolio.getPosition("IBM"), position);
    }

    @Test
    public void testGetNotContainedPosition() throws Exception {
        Iterator<Position> mockIterator = mock(Iterator.class);
        List<Position> mockList = mock(List.class);

        when(mockIterator.hasNext()).thenReturn(false);
        when(mockList.iterator()).thenReturn(mockIterator);
        Portfolio portfolio = new Portfolio(mockList);
        thrown.expect(IllegalArgumentException.class);
        portfolio.getPosition("IBM");
    }

    @Test
    public void testNumPositions() throws Exception {
        final int PORTFOLIO_SIZE = 5;
        List<Position> mockList = mock(List.class);
        when(mockList.size()).thenReturn(PORTFOLIO_SIZE);
        Portfolio portfolio = new Portfolio(mockList);
        assertEquals(portfolio.numPositions(), PORTFOLIO_SIZE);
    }

    @Test
    public void testCalculateNMV() throws Exception {
        List<Position> positionList = Arrays.asList(
                new Position("XYZ", 100),
                new Position("ZZZ", -100));
        assertEquals(-1000, new Portfolio(positionList).getNMV(closingPrices), 0.01);
    }

    @Test
    public void testGMV() throws Exception {
        List<Position> positionList = Arrays.asList(
                new Position("XYZ", 100),
                new Position("ZZZ", -100));
        assertEquals(30660, new Portfolio(positionList).getGMV(closingPrices), 0.01);
    }

    @Test
    public void testID() throws Exception {
        final String UUID = "b81266f2-3a1a-4d6d-be03-113c792a7583";
        final String name = "ARose";
        List<Position> positionList = Arrays.asList(
                new Position("XYZ", 100),
                new Position("ZZZ", -100));
        assertEquals(UUID,
                new Portfolio(positionList, name, UUID).getID());
    }

    @Test
    public void testName() throws Exception {
        final String name = "ARose";
        List<Position> positionList = Arrays.asList(
                new Position("XYZ", 100),
                new Position("ZZZ", -100));
        assertEquals(name, new Portfolio(positionList, name).getName());
    }

    @Test
    public void testItr() throws Exception {
        List<Position> positionList = Arrays.asList(
                new Position("XYZ", 100),
                new Position("ZZZ", -100));
        Portfolio portfolio = new Portfolio(positionList);
        int i = 0;
        for(Position p : portfolio) {
            i++;
            assertNotNull(p.getSide());
            assertNotNull(p.getSecurityID());
            assertNotEquals(p.getQuantity(), 0.0, 0.01);
        }
        assertEquals(i, 2);
    }
}