package com.wallerstein.var;

import com.wallerstein.model.Portfolio;

public interface VaRCalculator {
    /**
     * @param portfolio
     *            portfolio containing securities
     * @param percentile
     *            confidence level. 0.95 = 95%
     * @param interval
     *            number of days
     * @return VaR in portfolio value
     */
    double calculateWorstLoss(final Portfolio portfolio,
                              final double percentile,
                              final int interval);
}
