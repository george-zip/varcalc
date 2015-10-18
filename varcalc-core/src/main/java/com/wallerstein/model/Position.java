package com.wallerstein.model;

import java.security.InvalidParameterException;

import javax.xml.bind.annotation.XmlRootElement;

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
        this.securityID = securityID;
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

}
