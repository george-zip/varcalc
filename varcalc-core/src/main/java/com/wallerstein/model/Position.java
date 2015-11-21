package com.wallerstein.model;

import java.security.InvalidParameterException;

/**
 * Immutable position, consisting of a security ID and a positive or negative quantity.
 */
public final class Position {

    public enum Side {
        LONG, SHORT
    }

    private final String securityID;
    private final double quantity;

    public Position(final String securityID, final double quantity) {
        if(quantity == 0.0) {
            throw new InvalidParameterException("Quantity must be positive or negative.");
        }
        this.securityID = securityID.toUpperCase();
        this.quantity = quantity;
    }

    public String getSecurityID() {
        return securityID;
    }

    public double getQuantity() {
        return quantity;
    }

    public Side getSide() {
        if (quantity < 0.0) {
            return Side.SHORT;
        } else {
            return Side.LONG;
        }
    }

    @Override
    public boolean equals(Object o) {
        boolean retVal = false;
        if(o instanceof Position) {
            Position other = (Position) o;
            retVal = (securityID == other.securityID) && (quantity == other.quantity);
        }
        return retVal;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + securityID.hashCode();
        long qtyBits = Double.doubleToLongBits(quantity);
        result = 31 * result + (int) (qtyBits ^ (qtyBits >>> 32));
        return result;
    }
}
