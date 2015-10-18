package com.wallerstein.portfolio;

import com.wallerstein.model.Portfolio;
import com.wallerstein.model.Position;
import com.wallerstein.model.CPTimeSeries;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PortfolioTest {

    private TimeSeries ts1 = null;
    private TimeSeries ts2 = null;

    @Before
    public void setUp() throws Exception {
        this.ts1 = new TimeSeries("XYZ");
        ts1.add(new Day(2, 1, 2011), 181.8);
        ts1.add(new Day(3, 1, 2011), 167.3);
        ts1.add(new Day(4, 1, 2011), 153.8);
        ts1.add(new Day(5, 1, 2011), 167.6);
        ts1.add(new Day(6, 1, 2011), 158.8);
        ts1.add(new Day(7, 1, 2011), 148.3);

        this.ts2 = new TimeSeries("ZZZ");
        ts2.add(new Day(2, 1, 2011), 141.8);
        ts2.add(new Day(3, 1, 2011), 147.3);
        ts2.add(new Day(4, 1, 2011), 143.8);
        ts2.add(new Day(5, 1, 2011), 147.6);
        ts2.add(new Day(6, 1, 2011), 148.8);
        ts2.add(new Day(7, 1, 2011), 158.3);
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
        final int ARBITRARY_SIZE = 5;
        List<Position> mockList = mock(List.class);
        when(mockList.size()).thenReturn(ARBITRARY_SIZE);
        Portfolio portfolio = new Portfolio(mockList);
        assertEquals(portfolio.numPositions(), ARBITRARY_SIZE);
    }

    @Test
    public void testCalculateNMV() throws Exception {
        List<CPTimeSeries> l = new ArrayList<CPTimeSeries>();
        l.add(new CPTimeSeries("XYZ", ts1, 5));
        l.add(new CPTimeSeries("ZZZ", ts2, 5));

        List<Position> positionList = new ArrayList<Position>();
        positionList.add(new Position("XYZ", 100));
        positionList.add(new Position("ZZZ", 50));
        Portfolio p = new Portfolio(positionList);

        assertEquals(22745, p.getNMV(l), 0.01);
    }
}