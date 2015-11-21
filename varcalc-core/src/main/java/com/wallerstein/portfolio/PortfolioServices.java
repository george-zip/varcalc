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
     * @return ReturnsTimeSeries containing portfolio returns in dollar
     */
    public ReturnsTimeSeries calculateDollarReturns(
            final List<ClosingPriceTS> portfolioClosingPrices, final Portfolio portfolio) {

        if(!tsHaveSameNumberOfDataPoints(portfolioClosingPrices)) {
            throw new IllegalArgumentException(
                    "One of the time series in this portfolio does not have the same number of data points.");
        }

        TimeSeries portfolioReturns = new TimeSeries("PortfolioReturns");

        for (ClosingPriceTS positionClosingPrices : portfolioClosingPrices) {

            final String symbol = positionClosingPrices.getSymbol();
            final double price = positionClosingPrices.getPreviousClose();
            final double qty = portfolio.getPosition(symbol).getQuantity();
            final double positionMV = price * qty;

            ReturnsTimeSeries returnsForThisSymbols = ReturnsTimeSeries.fromCPTS(positionClosingPrices.getDataSet());

            @SuppressWarnings("unchecked")
            List<TimeSeriesDataItem> items = returnsForThisSymbols.getDataSet().getItems();

            for (TimeSeriesDataItem dataPoint : items) {
                final RegularTimePeriod timePeriod = dataPoint.getPeriod();
                final double periodReturn = dataPoint.getValue().doubleValue();
                TimeSeriesDataItem currentDataItem;
                double currentVal = 0.0;
                if ((currentDataItem = portfolioReturns.getDataItem(timePeriod)) != null) {
                    currentVal = currentDataItem.getValue().doubleValue();
                }
                portfolioReturns.addOrUpdate(timePeriod, currentVal + (periodReturn * positionMV));
            }
        }
        return new ReturnsTimeSeries(portfolioReturns);
    }

    public ReturnsTimeSeries calculatePercentageReturns(
            final List<ClosingPriceTS> portfolioClosingPrices, final Portfolio portfolio) {

        if(!tsHaveSameNumberOfDataPoints(portfolioClosingPrices)) {
            throw new IllegalArgumentException(
                    "One of the time series in this portfolio does not have the same number of data points.");
        }

        TimeSeries portfolioReturns = new TimeSeries("PortfolioReturns");

        for (ClosingPriceTS positionClosingPrices : portfolioClosingPrices) {

            final String symbol = positionClosingPrices.getSymbol();
            final double price = positionClosingPrices.getPreviousClose();
            final double qty = portfolio.getPosition(symbol).getQuantity();
            final double positionMV = price * qty;

            ReturnsTimeSeries returnsForThisSymbols = ReturnsTimeSeries.fromCPTS(positionClosingPrices.getDataSet());

            @SuppressWarnings("unchecked")
            List<TimeSeriesDataItem> items = returnsForThisSymbols.getDataSet().getItems();

            for (TimeSeriesDataItem dataPoint : items) {
                final RegularTimePeriod timePeriod = dataPoint.getPeriod();
                final double periodReturn = dataPoint.getValue().doubleValue();
                TimeSeriesDataItem currentDataItem;
                double currentVal = 0.0;
                if ((currentDataItem = portfolioReturns.getDataItem(timePeriod)) != null) {
                    currentVal = currentDataItem.getValue().doubleValue();
                }
                portfolioReturns.addOrUpdate(timePeriod, currentVal + (periodReturn * positionMV));
            }
        }
        return new ReturnsTimeSeries(portfolioReturns);
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