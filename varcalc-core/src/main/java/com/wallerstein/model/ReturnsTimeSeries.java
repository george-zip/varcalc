package com.wallerstein.model;

import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesDataItem;

import java.util.List;

public class ReturnsTimeSeries {

    // TODO: Add equality support

    private final TimeSeries dataSet;

    public ReturnsTimeSeries(final TimeSeries dataSet) {
        this.dataSet = dataSet;
    }

    /**
     * Create ReturnsTimeSeries from TimeSeries of closing prices
     * @param ts time series of closing prices
     * @return ReturnsTimeSeries
     */
    public static ReturnsTimeSeries fromCPTS(final TimeSeries ts) {
        return new ReturnsTimeSeries(calculateReturnsTimeSeries(ts));
    }

    public TimeSeries getDataSet() {
        return dataSet;
    }

    private static TimeSeries calculateReturnsTimeSeries(final TimeSeries ts) {
        TimeSeries retVal = new TimeSeries(ts.getDomainDescription());

        @SuppressWarnings("unchecked")
        List<TimeSeriesDataItem> l = ts.getItems();
        double lastClose = 0.0;
        for (TimeSeriesDataItem di : l) {
            if (lastClose != 0.0) {
                retVal.add(di.getPeriod(),
                        (di.getValue().doubleValue() / lastClose) - 1);
            }
            lastClose = di.getValue().doubleValue();
        }

        return retVal;
    }


}
