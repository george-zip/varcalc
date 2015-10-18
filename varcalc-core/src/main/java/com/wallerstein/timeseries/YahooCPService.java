package com.wallerstein.timeseries;

import com.wallerstein.model.CPTimeSeries;
import com.wallerstein.model.Portfolio;
import com.wallerstein.model.Position;

import java.util.ArrayList;
import java.util.List;

public class YahooCPService implements HistoricalClosingPrices {

    private static final int DEFAULT_YEARS = 4;

    public CPTimeSeries getClosingPricesForSymbol(final String symbol) {
        return getClosingPricesForSymbol(symbol, DEFAULT_YEARS);
    }

    public CPTimeSeries getClosingPricesForSymbol(final String symbol, final int yearInterval) {
        return new CPTimeSeries(symbol, YahooTimeSeriesFactory.getClosingPricesTS(symbol, yearInterval), yearInterval);
    }

    public List<CPTimeSeries> getClosingPricesForPortfolio(final Portfolio portfolio) {
        List<CPTimeSeries> portfolioTS = new ArrayList<CPTimeSeries>();
        for (Position position : portfolio) {
            CPTimeSeries cpTimeSeries = getClosingPricesForSymbol(
                    position.getSecurityID());
            portfolioTS.add(cpTimeSeries);
        }
        return portfolioTS;
    }

}
