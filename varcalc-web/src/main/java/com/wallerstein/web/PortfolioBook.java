package com.wallerstein.web;

import com.wallerstein.model.Portfolio;
import com.wallerstein.model.Position;

import java.util.*;
import javax.servlet.ServletContext;

public class PortfolioBook {

    private static String ATTRIBUTE_NAME = "PortfolioBook";
    private Map<String, Portfolio> portfolios = new TreeMap<>();
    private static int nextID = 0;

    {
        List<Position> pos1 = new ArrayList<Position>();
        pos1.add(new Position("IBM", 100));
        pos1.add(new Position("MS", 200));
        Portfolio p1 = new Portfolio(pos1, "foo");
        portfolios.put(p1.getID(), p1);

        List<Position> pos2 = new ArrayList<Position>();
        pos2.add(new Position("GOOG", 100));
        pos2.add(new Position("DUK", -200));
        Portfolio p2 = new Portfolio(pos2, "bar");
        portfolios.put(p2.getID(), p2);
    }

    public static PortfolioBook getPortfolioBook(ServletContext servletContext) {
        if(servletContext.getAttribute(ATTRIBUTE_NAME) == null) {
            PortfolioBook pb = new PortfolioBook(1);
            servletContext.setAttribute(ATTRIBUTE_NAME, pb);
        }
        return (PortfolioBook) servletContext.getAttribute(ATTRIBUTE_NAME);
    }

    public synchronized void addPortfolio(final Portfolio p) {
        portfolios.put(p.getID(), p);
    }

    public synchronized Portfolio getPortfolioByID(String id) {
        if(portfolios.containsKey(id)) {
            return portfolios.get(id);
        }
        throw new IllegalArgumentException(id + " not found");
    }

    public synchronized void deletePortfolioByID(String id) {
        if(portfolios.containsKey(id)) {
            portfolios.remove(id);
        }
        else {
            throw new IllegalArgumentException(id + " not found");
        }
    }

    public synchronized void updatePortfolioByID(String id, Portfolio portfolio) {
        if(portfolios.containsKey(id)) {
            portfolios.put(id, portfolio);
        }
        else {
            throw new IllegalArgumentException(id + " not found");
        }
    }

    public synchronized Portfolio[] getAllPortfolios() {
        return portfolios.values().toArray(new Portfolio[portfolios.size()]);
    }

    private PortfolioBook(int nextID) {
        this.nextID = nextID;
    }

}
