package com.wallerstein.model;

import org.jfree.data.time.TimeSeriesDataItem;

import java.util.Iterator;
import java.util.List;

/**
 * A immutable collection of positions.
 */
public final class Portfolio implements Iterable<Position> {

    private final List<Position> positions;

    public Portfolio(final List<Position> positions) {
        this.positions = positions;
    }

    public Position getPosition(final String securityID) {

        for (Position p : positions) {
            if (p.getSecurityID().equals(securityID)) {
                return p;
            }
        }

        throw new IllegalArgumentException(securityID + " not found");
    }

    public int numPositions() {
        return positions.size();
    }

    public Iterator<Position> iterator() {
        return positions.iterator();
    }

    public double getNMV(final List<CPTimeSeries> closingPrices) {
        double retVal = 0.0;

        for (CPTimeSeries cpTimeSeries : closingPrices) {
            int lastItem = cpTimeSeries.getDataSet().getItemCount() - 1;
            TimeSeriesDataItem di = cpTimeSeries.getDataSet().getDataItem(
                    lastItem);
            retVal += di.getValue().doubleValue()
                    * this.getPosition(cpTimeSeries.getSymbol()).getQuantity();
        }
        return retVal;
    }

}
