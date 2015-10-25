package com.wallerstein.model;

import org.jfree.data.time.TimeSeriesDataItem;

import javax.sound.sampled.Port;
import java.util.Iterator;
import java.util.List;

/**
 * A immutable collection of positions.
 */
public final class Portfolio implements Iterable<Position> {

    private final List<Position> positions;
    private final String name;

    public Portfolio(final List<Position> positions) {
        this.positions = positions;
        this.name = "";
    }

    public Portfolio(final List<Position> positions, final String name) {
        this.positions = positions;
        this.name = name;
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

    public String getName() {
        return name;
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
