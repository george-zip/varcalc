package com.wallerstein.timeseries;

import com.wallerstein.model.CPTimeSeries;
import com.wallerstein.model.Portfolio;

import java.util.List;

public interface HistoricalClosingPrices {
    CPTimeSeries getClosingPricesForSymbol(final String symbol, final int yearInterval);
    List<CPTimeSeries> getClosingPricesForPortfolio(final Portfolio portfolio);
}
