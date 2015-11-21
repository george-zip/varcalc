package com.wallerstein.model;

import java.util.*;

/**
 * An immutable collection of positions.
 */
public final class Portfolio implements Iterable<Position> {

    private List<Position> positions;
    private final String name;
    private final UUID id;

    public Portfolio(final List<Position> positions) {
        this.positions = flattenPositions(positions);
        this.name = "";
        this.id = UUID.randomUUID();
    }

    public Portfolio(final List<Position> positions, final String name) {
        this.positions = flattenPositions(positions);
        this.name = name;
        this.id = UUID.randomUUID();
    }

    public Portfolio(final List<Position> positions, final String name, String id) {
        this.positions = flattenPositions(positions);
        this.name = name;
        this.id = UUID.fromString(id);
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

    private interface ApplyIt {
        double apply(double input);
    }

    private double getMV(final List<ClosingPriceTS> closingPrices, ApplyIt f) {
        double retVal = 0.0;

        for (ClosingPriceTS closingPriceTS : closingPrices) {
            retVal += closingPriceTS.getPreviousClose()
                    * f.apply(getPosition(closingPriceTS.getSymbol()).getQuantity());
        }
        return retVal;
    }

    public double getNMV(final List<ClosingPriceTS> closingPrices) {
        return getMV(closingPrices, new ApplyIt() {
            public double apply(double input) {
                return input;
            }
        });
    }

    public double getGMV(final List<ClosingPriceTS> closingPrices) {
        return getMV(closingPrices, new ApplyIt() {
            public double apply(double input) {
                return Math.abs(input);
            }
        });
    }

    public String getID() {
        return id.toString();
    }

    private List<Position> flattenPositions(final List<Position> positions) {
        Map<String, Double> uniquePositions = new HashMap<>();
        for(Position p : positions) {
            Double qty = 0.0;
            if(uniquePositions.containsKey(p.getSecurityID())) {
                qty = uniquePositions.get(p.getSecurityID());
            }
            uniquePositions.put(p.getSecurityID(), qty + p.getQuantity());
        }
        List<Position> retVal = new ArrayList<>();
        for(Map.Entry<String, Double> entry : uniquePositions.entrySet()) {
            if(entry.getValue() != 0.0) {
                retVal.add(new Position(entry.getKey(), entry.getValue()));
            }
        }
        return retVal;
    }

}
