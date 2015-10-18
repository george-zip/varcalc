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

    private TimeSeries ts1 = null;

    @Before
    public void setUp() throws Exception {
        this.ts1 = new TimeSeries("timeSeries1");
        ts1.add(new Day(2, 1, 2011), 181.8);
        ts1.add(new Day(3, 1, 2011), 167.3);
        ts1.add(new Day(4, 1, 2011), 153.8);
        ts1.add(new Day(5, 1, 2011), 167.6);
        ts1.add(new Day(6, 1, 2011), 158.8);
        ts1.add(new Day(7, 1, 2011), 148.3);
    }

    @Test
    public void testFromCPTS() {
        double returns[] = { -0.079757976, -0.080693365, 0.089726918, -0.052505967, -0.066120907 };
        ReturnsTimeSeries rts = ReturnsTimeSeries.fromCPTS(ts1);
        @SuppressWarnings("unchecked")
        List<TimeSeriesDataItem> l = rts.getDataSet().getItems();
        int i = 0;
        for (TimeSeriesDataItem  di : l) {
            assertEquals(returns[i++], di.getValue().doubleValue(), 0.01);
        }
    }

}