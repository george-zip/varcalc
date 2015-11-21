package com.wallerstein.volatility;

import java.util.List;

import com.wallerstein.model.ClosingPriceTS;
import com.wallerstein.model.ReturnsTimeSeries;
import com.wallerstein.timeseries.TimeSeriesUtil;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesDataItem;

public class HistoricalVolCalculator implements VolatilityCalculator {

    @Override
    public double calculateDailyVolatility(List<ClosingPriceTS> portfClosingPrices) {
        double vol = 0.0;

        TimeSeries portfolioDeviation = new TimeSeries("Portfolio");
        for(ClosingPriceTS ts : portfClosingPrices) {
            final double avgPx = TimeSeriesUtil.getMean(ts.getDataSet());
            List<TimeSeriesDataItem> l = ts.getDataSet().getItems();
            for(TimeSeriesDataItem dataPoint : l) {
                final RegularTimePeriod timePeriod = dataPoint.getPeriod();
                final double variance = dataPoint.getValue().doubleValue() - avgPx;
                double varianceSq = variance * variance;
                if(portfolioDeviation.getDataItem(timePeriod) != null) {
                    varianceSq += portfolioDeviation.getDataItem(timePeriod).getValue().doubleValue();
                }
                portfolioDeviation.addOrUpdate(timePeriod, varianceSq);
            }
        }
        vol = Math.sqrt(TimeSeriesUtil.getMean(portfolioDeviation));
        return vol;
    }
}
