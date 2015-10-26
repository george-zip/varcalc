package com.wallerstein;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.wallerstein.model.Portfolio;
import com.wallerstein.model.Position;
import com.wallerstein.timeseries.HistoricalClosingPrices;
import com.wallerstein.timeseries.YahooCPService;
import com.wallerstein.var.HistoricVaRCalculator;
import com.wallerstein.var.VaRCalculator;
import com.wallerstein.volatility.MovingAvgVolatilityCalculator;
import com.wallerstein.volatility.VolatilityCalculator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

final class VaRCalcMain extends AbstractModule {

    @Override
    public void configure() {
        bind(VaRCalculator.class).to(HistoricVaRCalculator.class);
        bind(VolatilityCalculator.class).to(MovingAvgVolatilityCalculator.class);
        bind(HistoricalClosingPrices.class).to(YahooCPService.class);
    }

    private static Portfolio readPortfolioFile(final String fileName) throws IOException {
        List<Position> positions = new ArrayList<>();
        List<String> lines = FileUtils.readLines(new File(fileName));
        for (String s : lines) {
            String[] tokens = s.split(",");
            positions.add(new Position(tokens[0], Double.parseDouble(tokens[1])));
        }
        return new Portfolio(positions, fileName);
    }

    public static void main(final String[] args) {
        Injector injector = Guice.createInjector(new VaRCalcMain());
        PortfolioAnalyzer mc = injector.getInstance(PortfolioAnalyzer.class);
        String portfolioFileName = args[0];
        try {
            Portfolio portfolio = readPortfolioFile(portfolioFileName);
            mc.analyze(portfolio);
        }
        catch(IOException e) {
            e.printStackTrace(System.err);
        }
    }


//    public static void main(final String[] args) {
//        Console c = System.console();
//        if (c == null) {
//            System.err.println("No console.");
//            System.exit(1);
//        }
//
//        String nextSecurity;
//        List<Position> positions = new ArrayList<Position>();
//        while ((nextSecurity = c.readLine("Symbol Quantity: ")) != null) {
//            System.out.println(nextSecurity);
//            if (nextSecurity.contains("exit")) {
//                break;
//            }
//            StringTokenizer st = new StringTokenizer(nextSecurity);
//            positions.add(new Position(st.nextToken(), Double
//                    .parseDouble(st.nextToken())));
//        }
//        Portfolio portfolio = new Portfolio(positions);
//
//        VaRCalculator calc = new HistoricVaRCalculator();
//
//        Map<String, TimeSeries> portfolioTS = PortfolioUtil
//                .getPortfolioTimeSeries(portfolio);
//
//        NumberFormat cf = NumberFormat.getCurrencyInstance(Locale.US);
//        NumberFormat pf = NumberFormat.getPercentInstance();
//
//        System.out.println("Portfolio MV: "
//                + cf.format(PortfolioUtil.calculateNMV(
//                portfolioTS, portfolio)));
//        System.out.println("Historic 1 day VaR @ 95%: "
//                + cf.format(calc.calculate(portfolio, PERCENTILE, 1)));
//
//        VolatilityCalculator vc = new MovingAvgVolatilityCalculator();
//        ReturnsTimeSeries returnsTimeSeries = mc.portfolioServices.calculatePortfolioReturns(
//                portfolioTS, portfolio);
//
//        double volatility = vc.calculateVolatility(returnsTimeSeries.getDataSet());
//
//        System.out.println("Simple portfolio volatility: " + pf.format(
//                volatility));
//    }
}
