package com.wallerstein.portfolio;

import com.wallerstein.model.Portfolio;
import com.wallerstein.model.ClosingPriceTS;
import com.wallerstein.model.ReturnsTimeSeries;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesDataItem;

import java.util.List;

public final class PortfolioServices {

    /**
     * Given list of time series of closing prices and a portfolio, it generates the time
     * series of portfolio returns based on log returns
     * @param portfolioClosingPrices list of price return series
     * @param portfolio portfolio of securities
     * @return ReturnsTimeSeries containing portfolio returns
     */
    public ReturnsTimeSeries calculatePortfolioReturns(
            final List<ClosingPriceTS> portfolioClosingPrices, final Portfolio portfolio) {

        if(!tsHaveSameNumberOfDataPoints(portfolioClosingPrices)) {
            throw new IllegalArgumentException(
                    "One of the time series in this portfolio does not have the same number of data points.");
        }

        TimeSeries portfolioReturns = new TimeSeries("PortfolioReturns");

        for (ClosingPriceTS positionClosingPrices : portfolioClosingPrices) {

            @SuppressWarnings("unchecked")
            List<TimeSeriesDataItem> items = positionClosingPrices.getDataSet().getItems();

            for (TimeSeriesDataItem tsDataItem : items) {
                final RegularTimePeriod timePeriod = tsDataItem.getPeriod();
                final double price = tsDataItem.getValue().doubleValue();
                final double qty = portfolio.getPosition(positionClosingPrices.getSymbol()).getQuantity();
                TimeSeriesDataItem currentDataItem;
                double currentVal = 0.0;
                if ((currentDataItem = portfolioReturns.getDataItem(timePeriod)) != null) {
                    currentVal = currentDataItem.getValue().doubleValue();
                }
                portfolioReturns.addOrUpdate(timePeriod, currentVal + (qty * price));
            }
        }
        return ReturnsTimeSeries.fromCPTS(portfolioReturns);
    }

    private boolean tsHaveSameNumberOfDataPoints(final List<ClosingPriceTS> tsList) {
        final int tsSize = tsList.get(0).getDataSet().getItemCount();
        for(ClosingPriceTS cpts : tsList) {
            if(cpts.getDataSet().getItemCount() != tsSize) {
                return false;
            }
        }
        return true;
    }
}