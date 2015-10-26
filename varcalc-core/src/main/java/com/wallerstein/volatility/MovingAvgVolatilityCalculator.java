package com.wallerstein.volatility;

import java.util.List;

import com.wallerstein.model.ReturnsTimeSeries;
import org.jfree.data.time.TimeSeriesDataItem;

public class MovingAvgVolatilityCalculator implements VolatilityCalculator {

    public final double calculateVolatility(final ReturnsTimeSeries returnsTimeSeries) {

        double summation = 0.0;

        @SuppressWarnings("unchecked")
        List<TimeSeriesDataItem> l = returnsTimeSeries.getDataSet().getItems();
        for (TimeSeriesDataItem di : l) {
            summation += Math.pow(di.getValue().doubleValue(), 2);
        }

        return Math.sqrt(summation / returnsTimeSeries.getDataSet().getItemCount());
    }

}
