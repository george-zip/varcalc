package com.wallerstein.var;

import com.google.inject.Inject;
import com.wallerstein.model.Portfolio;
import com.wallerstein.portfolio.PortfolioServices;
import com.wallerstein.model.ClosingPriceTS;
import com.wallerstein.timeseries.HistoricalClosingPrices;
import com.wallerstein.model.ReturnsTimeSeries;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesDataItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Calculate Value at Risk using simple historic model.
 */
public final class HistoricVaRCalculator implements VaRCalculator {

    private final HistoricalClosingPrices closingPricesSource;
    private final PortfolioServices portfolioServices;

    @Inject
    public HistoricVaRCalculator(HistoricalClosingPrices closingPricesSource, PortfolioServices portfolioServices) {
        this.closingPricesSource = closingPricesSource;
        this.portfolioServices = portfolioServices;
    }

    /**
     * Calculate Historic VaR.
     * @param portfolio
     *            portfolio containing securities
     * @param percentile
     *            confidence level. 0.95 = 95%
     * @param days number of days
     * @return calculated VaR
     */
    public double calculateWorstLoss(final Portfolio portfolio,
                                     final double percentile,
                                     final int days) {

        if (portfolio.numPositions() < 1 || days < 1) {
            return 0.0;
        }

        List<ClosingPriceTS> portfClosingPrices = closingPricesSource.getClosingPricesForPortfolio(portfolio);
        if (portfClosingPrices.size() > 0) {
            ReturnsTimeSeries portfolioReturns = portfolioServices.calculatePortfolioReturns(portfClosingPrices, portfolio);
            List<TimeSeriesDataItem> returnsList = sortReturns(portfolioReturns.getDataSet());
            double portfolioNMV = portfolio.getNMV(portfClosingPrices);
            return Math.min(
                    scaleVaR(getNthPercentile(returnsList, percentile), days)
                            * portfolioNMV, portfolioNMV);

        }

        return 0.0;
    }

    private List<TimeSeriesDataItem> sortReturns(
            final TimeSeries portfolioReturns) {
        @SuppressWarnings("unchecked")
        List<TimeSeriesDataItem> returnsList = portfolioReturns.getItems();
        List<TimeSeriesDataItem> retVal = new ArrayList<>(returnsList);

        class TSSort implements Comparator<TimeSeriesDataItem> {

            public int compare(final TimeSeriesDataItem o1,
                    final TimeSeriesDataItem o2) {
                if (o1.getValue().doubleValue() < o2.getValue()
                        .doubleValue()) {
                    return 1;
                }
                if (o1.getValue().doubleValue() > o2.getValue()
                        .doubleValue()) {
                    return -1;
                }
                return 0;
            }

        }

        try {
            Collections.sort(retVal, new TSSort());
        } catch (Exception e) {
            throw new UnsupportedOperationException("Error while sorting returns: " + e.getMessage(), e);
        }

        return retVal;
    }

    private double getNthPercentile(final List<TimeSeriesDataItem> returnsList,
            final double percentile) {
        double retVal = 0.0;
        int idx1 = (int) Math.floor((returnsList.size() - 1) * percentile);
        int idx2 = (int) Math.ceil((returnsList.size() - 1) * percentile);
        if(idx1 == idx2) {
            retVal = returnsList.get(idx1).getValue().doubleValue();
        }
        else {
            // poor man's linear interpolation
            retVal = (returnsList.get(idx1).getValue().doubleValue() +
                    returnsList.get(idx2).getValue().doubleValue()) / 2.0;
        }
        return retVal;
    }

    private double scaleVaR(final double unscaledVaR, final int days) {
        if (days <= 1) {
            return unscaledVaR;
        } else {
            return unscaledVaR * Math.sqrt(days);
        }
    }
}
