package com.wallerstein.var;

import com.google.inject.Inject;
import com.wallerstein.model.Portfolio;
import com.wallerstein.portfolio.PortfolioServices;
import com.wallerstein.model.CPTimeSeries;
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
    public double calculate(final Portfolio portfolio,
                            final double percentile,
                            final int days) {

        if (portfolio.numPositions() < 1 || days < 1) {
            return 0.0;
        }

        List<CPTimeSeries> portfClosingPrices = closingPricesSource.getClosingPricesForPortfolio(portfolio);
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
        List<TimeSeriesDataItem> returnsList2 = new ArrayList<
                TimeSeriesDataItem>(returnsList);

        class Foobar implements Comparator<TimeSeriesDataItem> {

            public int compare(final TimeSeriesDataItem arg0,
                    final TimeSeriesDataItem arg1) {
                if (arg0.getValue().doubleValue() < arg1.getValue()
                        .doubleValue()) {
                    return -1;
                }
                if (arg0.getValue().doubleValue() > arg1.getValue()
                        .doubleValue()) {
                    return 1;
                }
                return 0;
            }

        }

        try {
            Collections.sort(returnsList2, new Foobar());
        } catch (Exception e) {
            System.out.println("e = " + e);
        }
        return returnsList2;
    }

    private double getNthPercentile(final List<TimeSeriesDataItem> returnsList,
            final double percentile) {
        return returnsList
                .get((int) Math.floor(returnsList.size() * percentile))
                .getValue().doubleValue();
    }

    private double scaleVaR(final double unscaledVaR, final int days) {
        if (days <= 1) {
            return unscaledVaR;
        } else {
            return unscaledVaR * Math.sqrt(days);
        }
    }
}
