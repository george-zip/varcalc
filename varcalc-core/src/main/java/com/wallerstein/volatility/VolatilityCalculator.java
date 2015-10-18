package com.wallerstein.volatility;

import com.wallerstein.model.ReturnsTimeSeries;
import org.jfree.data.time.TimeSeries;

public interface VolatilityCalculator {
    /**
     * Calculates daily volatility for a given time series.
     * @param returnsTimeSeries
     *            time series
     * @return volatility
     */
    double calculateVolatility(ReturnsTimeSeries returnsTimeSeries);
}
