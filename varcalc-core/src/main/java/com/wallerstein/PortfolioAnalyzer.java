package com.wallerstein;

import com.google.inject.Inject;
import com.wallerstein.model.CPTimeSeries;
import com.wallerstein.model.Portfolio;
import com.wallerstein.model.Position;
import com.wallerstein.model.ReturnsTimeSeries;
import com.wallerstein.portfolio.PortfolioServices;
import com.wallerstein.timeseries.HistoricalClosingPrices;
import com.wallerstein.var.VaRCalculator;
import com.wallerstein.volatility.VolatilityCalculator;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PortfolioAnalyzer {

    private final VaRCalculator vaRCalculator;
    private final VolatilityCalculator volatilityCalculator;
    private final HistoricalClosingPrices closingPricesSource;
    private final PortfolioServices portfolioServices;

    private final double VAR_PERCENTILE = 0.95;
    private final int VAR_DAYS = 1;

    @Inject
    public PortfolioAnalyzer(VaRCalculator vaRCalculator, VolatilityCalculator volatilityCalculator,
                             HistoricalClosingPrices closingPricesSource, PortfolioServices portfolioServices) {
        this.vaRCalculator = vaRCalculator;
        this.volatilityCalculator = volatilityCalculator;
        this.closingPricesSource = closingPricesSource;
        this.portfolioServices = portfolioServices;
    }

    public void analyze(Portfolio portfolio) {
        System.out.println("Positions");
        System.out.println("=========");
        for (Position p : portfolio) {
            String output = p.getSide() + " " + Math.abs(p.getQuantity()) + " "
                    + p.getSecurityID();
            System.out.println(output);
        }
        NumberFormat cf = NumberFormat.getCurrencyInstance(Locale.US);
        List<CPTimeSeries> portfClosingPrices = closingPricesSource.getClosingPricesForPortfolio(portfolio);
        System.out.println("\nPortfolio MV as of yesterday's close: " + cf.format(portfolio.getNMV(portfClosingPrices)));

        System.out.println("Historic " + VAR_DAYS + " day(s) VaR @ " + VAR_PERCENTILE * 100 + "%: "
                + cf.format(vaRCalculator.calculate(portfolio, VAR_PERCENTILE, VAR_DAYS)));

        ReturnsTimeSeries returnsTimeSeries = portfolioServices.calculatePortfolioReturns(portfClosingPrices, portfolio);
        double volatility = volatilityCalculator.calculateVolatility(returnsTimeSeries);

        System.out.println("Simple portfolio volatility: " + volatility);
    }
}
