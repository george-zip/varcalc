package com.wallerstein.portfolio;

import com.wallerstein.model.Portfolio;
import com.wallerstein.model.CPTimeSeries;
import com.wallerstein.model.ReturnsTimeSeries;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesDataItem;

import java.util.List;

public final class PortfolioServices {

    /**
     * Given list of time series of closing prices and a portfolio, it calculates the time
     * series of portfolio returns.
     * @param portfolioTS list of price return series
     * @param portfolio portfolio of securities
     * @return ReturnsTimeSeries containing portfolio returns
     */
    public ReturnsTimeSeries calculatePortfolioReturns(
            final List<CPTimeSeries> portfolioTS, final Portfolio portfolio) {

        TimeSeries portfolioValues = new TimeSeries("PortfolioValues");

        int numEntries = -1;
        for (CPTimeSeries cpTimeSeries : portfolioTS) {

            @SuppressWarnings("unchecked")
            List<TimeSeriesDataItem> l = cpTimeSeries.getDataSet().getItems();

            if (numEntries != -1 && l.size() != numEntries) {
                throw new IllegalArgumentException(
                        "Time series is not equal for " + cpTimeSeries.getSymbol());
            }

            numEntries = l.size();

            for (TimeSeriesDataItem di : l) {
                RegularTimePeriod per = di.getPeriod();
                double posPx = di.getValue().doubleValue();
                double posQty = portfolio.getPosition(cpTimeSeries.getSymbol()).getQuantity();
                TimeSeriesDataItem portfDI;
                // TODO: clean up this assignment
                double portfolioVal = 0.0;
                if ((portfDI = portfolioValues.getDataItem(per)) != null) {
                    portfolioVal = portfDI.getValue().doubleValue();
                }
                portfolioValues.addOrUpdate(per, portfolioVal
                        + (posQty * posPx));
            }
        }
        return ReturnsTimeSeries.fromCPTS(portfolioValues);
    }
}