package com.wallerstein.timeseries;

import java.util.Iterator;
import java.util.List;

import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesDataItem;

/*
 * Utility class for jfree TimeSeries class.
 */
final class TimeSeriesUtil {

    /**
     * Calculates the sum of values in the time series.
     * @param ts
     *            TimeSeries to sum
     * @return sum of values in time series
     * */
    static double getSum(final TimeSeries ts) {
        double retVal = 0.0;

        @SuppressWarnings("unchecked")
        List<TimeSeriesDataItem> l = ts.getItems();
        for (TimeSeriesDataItem di : l) {
            retVal += di.getValue().doubleValue();
        }

        return retVal;
    }

    /**
     * Calculate average of values in ts.
     * @param ts TimeSeries
     * @return average
     */
    static double getMean(final TimeSeries ts) {
        return getSum(ts) / ts.getItemCount();
    }

    static double getVariance(final TimeSeries ts) {
        double retVal = 0.0;

        final double mean = getMean(ts);

        @SuppressWarnings("unchecked")
        List<TimeSeriesDataItem> l = ts.getItems();
        for (TimeSeriesDataItem di : l) {
            retVal += Math.pow(di.getValue().doubleValue() - mean, 2);
        }

        return retVal / ts.getItemCount();
    }

    static double getCovariance(final TimeSeries ts1,
                                       final TimeSeries ts2) {
        if (ts1.getItemCount() != ts2.getItemCount()) {
            throw new IllegalArgumentException(
                    "Cannot calculate covariance on unequal series");
        }

        @SuppressWarnings("unchecked")
        Iterator<TimeSeriesDataItem> i1 = ts1.getItems().iterator();
        @SuppressWarnings("unchecked")
        Iterator<TimeSeriesDataItem> i2 = ts2.getItems().iterator();

        final double mean1 = getMean(ts1);
        final double mean2 = getMean(ts2);
        double coVar = 0.0;

        for (; i1.hasNext() && i2.hasNext();) {

            TimeSeriesDataItem di1 = i1.next();
            TimeSeriesDataItem di2 = i2.next();

            if (!di1.getPeriod().equals(di2.getPeriod())) {
                throw new IllegalArgumentException(
                        "Unequal time series period: " + di1.getPeriod()
                                + " vs " + di2.getPeriod());
            }

            coVar += (di1.getValue().doubleValue() - mean1)
                    * (di2.getValue().doubleValue() - mean2);
        }

        return coVar / ts1.getItemCount();
    }

    static double getCorrelation(final TimeSeries ts1,
                                        final TimeSeries ts2) {
        return getCovariance(ts1, ts2)
                / Math.sqrt(getVariance(ts1) * getVariance(ts2));
    }

    private TimeSeriesUtil() {
        throw new AssertionError();
    }
}
