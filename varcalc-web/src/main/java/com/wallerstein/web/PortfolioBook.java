package com.wallerstein.web;

import com.wallerstein.model.Portfolio;
import com.wallerstein.model.Position;

import java.util.*;
import javax.servlet.ServletContext;

public class PortfolioBook {

    private static String ATTRIBUTE_NAME = "PortfolioBook";
    private Map<Integer, Portfolio> portfolios = new TreeMap<>();
    private static int nextID = 0;

    {
        portfolios.put(nextID++, new Portfolio(new ArrayList<Position>(), "foo"));
        portfolios.put(nextID++, new Portfolio(new ArrayList<Position>(), "bar"));
    }

    public static PortfolioBook getPortfolioBook(ServletContext servletContext) {
        if(servletContext.getAttribute(ATTRIBUTE_NAME) == null) {
            PortfolioBook pb = new PortfolioBook(1);
            servletContext.setAttribute(ATTRIBUTE_NAME, pb);
        }
        return (PortfolioBook) servletContext.getAttribute(ATTRIBUTE_NAME);
    }

    public synchronized void addPortfolio(final Portfolio p) {
        portfolios.put(new Integer(nextID++), p);
    }

    public synchronized Portfolio getPortfolioByID(Integer id) {
        if(portfolios.containsKey(id)) {
            return portfolios.get(id);
        }
        throw new IllegalArgumentException(id + " not found");
    }

    public synchronized Portfolio[] getAllPortfolios() {
        return portfolios.values().toArray(new Portfolio[portfolios.size()]);
    }

    private PortfolioBook(int nextID) {
        this.nextID = nextID;
    }

}
