package com.wallerstein.timeseries;

import com.wallerstein.model.ClosingPriceTS;
import com.wallerstein.model.Portfolio;
import com.wallerstein.model.Position;

import java.util.ArrayList;
import java.util.List;

public class YahooCPService implements HistoricalClosingPrices {

    private static final int DEFAULT_YEARS = 4;

    public ClosingPriceTS getClosingPricesForSymbol(final String symbol) {
        return getClosingPricesForSymbol(symbol, DEFAULT_YEARS);
    }

    public ClosingPriceTS getClosingPricesForSymbol(final String symbol, final int yearInterval) {
        return new ClosingPriceTS(symbol, YahooTimeSeriesFactory.getClosingPricesTS(symbol, yearInterval), yearInterval);
    }

    public List<ClosingPriceTS> getClosingPricesForPortfolio(final Portfolio portfolio) {
        List<ClosingPriceTS> portfolioTS = new ArrayList<>();
        for (Position position : portfolio) {
            ClosingPriceTS closingPriceTS = getClosingPricesForSymbol(
                    position.getSecurityID());
            portfolioTS.add(closingPriceTS);
        }
        return portfolioTS;
    }

}
