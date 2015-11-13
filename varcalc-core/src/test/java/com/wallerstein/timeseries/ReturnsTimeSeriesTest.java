package com.wallerstein.timeseries;

import com.wallerstein.model.ReturnsTimeSeries;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesDataItem;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ReturnsTimeSeriesTest {

    private TimeSeries ts = null;

    @Before
    public void setUp() throws Exception {
        ts = new TimeSeries("timeSeries1");
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
    }

    @Test
    public void testFromCPTS() {
        // log returns
        double returns[] = { -0.0017, 0.0103, -0.0021, -0.0071, -0.0067, 0.0013, -0.0091, -0.0105, -0.0044 };

        ReturnsTimeSeries rts = ReturnsTimeSeries.fromCPTS(ts);

        @SuppressWarnings("unchecked")
        List<TimeSeriesDataItem> l = rts.getDataSet().getItems();
        int i = 0;
        for (TimeSeriesDataItem  di : l) {
            assertEquals(returns[i++], di.getValue().doubleValue(), 0.01);
        }
    }

}