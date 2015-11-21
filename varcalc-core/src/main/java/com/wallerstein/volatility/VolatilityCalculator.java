package com.wallerstein.volatility;

import com.wallerstein.model.ClosingPriceTS;

import java.util.List;

public interface VolatilityCalculator {
    /**
     * Calculates daily volatility for a given time series.
     * @param portfClosingPrices closing prices for portfolio
     * @return volatility
     */
    double calculateDailyVolatility(List<ClosingPriceTS> portfClosingPrices);
}
