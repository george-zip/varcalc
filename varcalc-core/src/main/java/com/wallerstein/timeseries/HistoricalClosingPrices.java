package com.wallerstein.timeseries;

import com.wallerstein.model.ClosingPriceTS;
import com.wallerstein.model.Portfolio;

import java.util.List;

public interface HistoricalClosingPrices {
    ClosingPriceTS getClosingPricesForSymbol(final String symbol, final int yearInterval);
    List<ClosingPriceTS> getClosingPricesForPortfolio(final Portfolio portfolio);
}
