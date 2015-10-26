package com.wallerstein.timeseries;

import static org.junit.Assert.*;

import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.junit.Before;
import org.junit.Test;

public class TimeSeriesUtilTest {

	private TimeSeries ts1 = null;
	private TimeSeries ts2 = null;
	private TimeSeries ts3 = null; // unequal TS - date is different
	private TimeSeries ts4 = null; // unequal sizes
	
	@Before
	public void setUp() throws Exception {
		this.ts1 = new TimeSeries("timeSeries1");
		ts1.add(new Day(2, 1, 2011), 181.8);
		ts1.add(new Day(3, 1, 2011), 167.3);
		ts1.add(new Day(4, 1, 2011), 153.8);
		ts1.add(new Day(5, 1, 2011), 167.6);
		ts1.add(new Day(6, 1, 2011), 158.8);
		ts1.add(new Day(7, 1, 2011), 148.3);
		
		this.ts2 = new TimeSeries("timeSeries2");
		ts2.add(new Day(2, 1, 2011), 141.8);
		ts2.add(new Day(3, 1, 2011), 147.3);
		ts2.add(new Day(4, 1, 2011), 143.8);
		ts2.add(new Day(5, 1, 2011), 147.6);
		ts2.add(new Day(6, 1, 2011), 148.8);
		ts2.add(new Day(7, 1, 2011), 158.3);

		this.ts3 = new TimeSeries("timeSeries3");
		ts3.add(new Day(2, 1, 2011), 181.8);
		ts3.add(new Day(3, 1, 2011), 167.3);
		ts3.add(new Day(4, 1, 2011), 153.8);
		ts3.add(new Day(5, 1, 2011), 167.6);
		ts3.add(new Day(6, 1, 2011), 158.8);
		ts3.add(new Day(8, 1, 2011), 148.3);
		
		this.ts4 = new TimeSeries("timeSeries4");
		ts4.add(new Day(2, 1, 2011), 141.8);
		ts4.add(new Day(3, 1, 2011), 147.3);
		ts4.add(new Day(4, 1, 2011), 143.8);
		ts4.add(new Day(6, 1, 2011), 148.8);
		ts4.add(new Day(7, 1, 2011), 158.3);
	}

	@Test
	public void testGetSum() {
		assertEquals(977.6, TimeSeriesUtil.getSum(ts1), 0.1);
	}

	@Test
	public void testGetMean() {
		assertEquals(162.9333333, TimeSeriesUtil.getMean(ts1), 0.1);
	}

	@Test
	public void testGetVariance() {
		assertEquals(118.5722222, TimeSeriesUtil.getVariance(ts1), 0.1);
	}

	@Test(expected= IllegalArgumentException.class)
	public void testGetCovariance1() {
		TimeSeriesUtil.getCovariance(ts1, ts3);
	}
	
	@Test(expected= IllegalArgumentException.class)
	public void testGetCovariance2() {
		TimeSeriesUtil.getCovariance(ts1, ts4);		
	}

	@Test
	public void testGetCovariance3() {
		assertEquals(-39.59444444, TimeSeriesUtil.getCovariance(ts1, ts2), 0.1);		
	}

	@Test
	public void testGetCorrelation() {
		assertEquals(-0.696703641, TimeSeriesUtil.getCorrelation(ts1, ts2), 0.1);		
	}

}
