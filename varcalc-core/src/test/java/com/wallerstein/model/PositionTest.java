package com.wallerstein.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.security.InvalidParameterException;

import static org.junit.Assert.assertEquals;

public class PositionTest {

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    @Test
    public void testGetSecurityID() throws Exception {
        Position p = new Position("IBM", 100.0);
        assertEquals(p.getSecurityID(), "IBM");
    }

    @Test
    public void testGetQuantity() throws Exception {
        Position p = new Position("IBM", 100.0);
        assertEquals(p.getQuantity(), 100,0);
    }

    @Test
    public void testGetSide() throws Exception {
        Position p1 = new Position("IBM", 100.0);
        assertEquals(p1.getSide(), Position.Side.LONG);
        Position p2 = new Position("IBM", -100.0);
        assertEquals(p2.getSide(), Position.Side.SHORT);
    }

    @Test
    public void testBadQty() throws Exception {
        thrown.expect(InvalidParameterException.class);
        new Position("IBM", 0.0);
    }
}