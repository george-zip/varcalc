package com.wallerstein.model;

import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesDataItem;

public final class ClosingPriceTS {

    // TODO: Add equality support

    private final String symbol;
    private final TimeSeries dataSet;
    private final int yearInterval;

    public ClosingPriceTS(final String symbol, final TimeSeries dataSet, final int yearInterval) {
        this.symbol = symbol;
        this.dataSet = dataSet;
        this.yearInterval = yearInterval;
    }

    public String getSymbol() {
        return symbol;
    }

    public TimeSeries getDataSet() {
        return dataSet;
    }

    public int getYearInterval() {
        return yearInterval;
    }

    public double getPreviousClose() {
        TimeSeriesDataItem di = getDataSet().getDataItem(getDataSet().getItemCount() - 1);
        return di.getValue().doubleValue();
    }
}