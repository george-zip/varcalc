package com.wallerstein.web;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wallerstein.model.*;
import com.wallerstein.portfolio.PortfolioServices;
import com.wallerstein.timeseries.HistoricalClosingPrices;
import com.wallerstein.timeseries.YahooCPService;
import com.wallerstein.var.HistoricVaRCalculator;
import com.wallerstein.var.VaRCalculator;
import com.wallerstein.volatility.HistoricalVolCalculator;
import com.wallerstein.volatility.VolatilityCalculator;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("portfolios")
public final class PortfoliosService {

    private final HistoricalClosingPrices closingPricesSource = new YahooCPService();
    private final PortfolioServices portfolioServices = new PortfolioServices();
    private final VaRCalculator vaRCalculator = new HistoricVaRCalculator
            (closingPricesSource, portfolioServices);
    private final VolatilityCalculator volatilityCalculator = new HistoricalVolCalculator();

    private final int UnprocessableEntity = 442;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Portfolio[] get(@Context ServletContext context) {
        return PortfolioBook.getPortfolioBook(context).getAllPortfolios();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(Portfolio portfolio, @Context ServletContext context) {
        try {
            PortfolioBook.getPortfolioBook(context).addPortfolio(portfolio);
        }
        catch(IllegalArgumentException e) {
            return Response.status(UnprocessableEntity).build();
        }
        return Response.ok().build();
    }

    @GET
    @Path("{portfolio_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Portfolio getByID(@Context ServletContext context,
                         @PathParam("portfolio_id") String id) {
        return PortfolioBook.getPortfolioBook(context).getPortfolioByID(id);
    }

    @DELETE
    @Path("{portfolio_id}")
    public Response delete(@Context ServletContext context,
                            @PathParam("portfolio_id") String id) {
        PortfolioBook.getPortfolioBook(context).deletePortfolioByID(id);
        return Response.ok().build();
    }

    @GET
    @Path("{portfolio_id}/positions")
    @Produces(MediaType.APPLICATION_JSON)
    public Position[] getPositions(@Context ServletContext context,
                             @PathParam("portfolio_id") String id) {
        Portfolio portfolio = PortfolioBook.getPortfolioBook(context).getPortfolioByID(id);
        Position[] retVal = new Position[portfolio.numPositions()];
        int index = 0;
        for(Position p : portfolio) {
            retVal[index++] = p;
        }
        return retVal;
    }

    @POST
    @Path("{portfolio_id}/positions")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPosition(Position position, @Context ServletContext context,
                                   @PathParam("portfolio_id") String id) {
        Portfolio portfolio = PortfolioBook.getPortfolioBook(context).getPortfolioByID(id);
        List<Position> positionList = new ArrayList<>(portfolio.numPositions() + 1);
        for(Position pos : portfolio) {
            positionList.add(pos);
        }
        positionList.add(position);
        Portfolio newPortfolio = new Portfolio(positionList, portfolio.getName(), portfolio.getID());
        PortfolioBook.getPortfolioBook(context).updatePortfolioByID(id, newPortfolio);
        return Response.ok().build();
    }

    @POST
    @Path("{portfolio_id}/positions/{symbol}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePosition(Position position, @Context ServletContext context,
                                   @PathParam("portfolio_id") String id,
                                   @PathParam("symbol") String symbol) {
        Portfolio portfolio = PortfolioBook.getPortfolioBook(context).getPortfolioByID(id);
        List<Position> positionList = new ArrayList<>(portfolio.numPositions());
        for(Position pos : portfolio) {
            if(pos.getSecurityID().equals(symbol)) {
                positionList.add(position);
            }
            else {
                positionList.add(pos);
            }
        }
        Portfolio newPortfolio = new Portfolio(positionList, portfolio.getName(), portfolio.getID());
        PortfolioBook.getPortfolioBook(context).updatePortfolioByID(id, newPortfolio);
        return Response.ok().build();
    }

    @DELETE
    @Path("{portfolio_id}/positions/{symbol}")
    @Produces(MediaType.APPLICATION_JSON)
    //@Consumes(MediaType.APPLICATION_JSON)
    public Response deletePosition(@Context ServletContext context,
                                   @PathParam("portfolio_id") String id,
                                   @PathParam("symbol") String symbol) {
        Portfolio portfolio = PortfolioBook.getPortfolioBook(context).getPortfolioByID(id);
        List<Position> positionList = new ArrayList<>(portfolio.numPositions());
        for(Position pos : portfolio) {
            if(!pos.getSecurityID().equals(symbol)) {
                positionList.add(pos);
            }
        }
        Portfolio newPortfolio = new Portfolio(positionList, portfolio.getName(), portfolio.getID());
        PortfolioBook.getPortfolioBook(context).updatePortfolioByID(id, newPortfolio);
        return Response.ok().build();
    }

    @GET
    @Path("{portfolio_id}/var")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonElement getVaR(@Context ServletContext context, @PathParam("portfolio_id") String id,
               @DefaultValue("0.95") @QueryParam("var_percentile") double varPercentile,
               @DefaultValue("1") @QueryParam("var_days") int varDays) {
        Portfolio portfolio = PortfolioBook.getPortfolioBook(context).getPortfolioByID(id);
        double var = vaRCalculator.calculateWorstLoss(portfolio, varPercentile, varDays);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("var", var);
        JsonObject jsonDataObject = new JsonObject();
        jsonDataObject.add("data", jsonObject);
        return jsonDataObject;
    }

    @GET
    @Path("{portfolio_id}/nmv")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonElement getNMV(@Context ServletContext context, @PathParam("portfolio_id") String id) {
        Portfolio portfolio = PortfolioBook.getPortfolioBook(context).getPortfolioByID(id);
        List<ClosingPriceTS> portfClosingPrices = closingPricesSource.getClosingPricesForPortfolio(portfolio);
        double nmv = portfolio.getNMV(portfClosingPrices);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("nmv", nmv);
        JsonObject jsonDataObject = new JsonObject();
        jsonDataObject.add("data", jsonObject);
        return jsonDataObject;
    }

    @GET
    @Path("{portfolio_id}/gmv")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonElement getGMV(@Context ServletContext context, @PathParam("portfolio_id") String id) {
        Portfolio portfolio = PortfolioBook.getPortfolioBook(context).getPortfolioByID(id);
        List<ClosingPriceTS> portfClosingPrices = closingPricesSource.getClosingPricesForPortfolio(portfolio);
        double nmv = portfolio.getGMV(portfClosingPrices);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("gmv", nmv);
        JsonObject jsonDataObject = new JsonObject();
        jsonDataObject.add("data", jsonObject);
        return jsonDataObject;
    }

    @GET
    @Path("{portfolio_id}/vol")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonElement getVol(@Context ServletContext context, @PathParam("portfolio_id") String id) {
        Portfolio portfolio = PortfolioBook.getPortfolioBook(context).getPortfolioByID(id);
        double volatility = 0.0;
        if(portfolio.numPositions() > 0) {
            List<ClosingPriceTS> portfClosingPrices = closingPricesSource.getClosingPricesForPortfolio(portfolio);
            volatility = volatilityCalculator.calculateDailyVolatility(portfClosingPrices);
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("vol", volatility);
        JsonObject jsonDataObject = new JsonObject();
        jsonDataObject.add("data", jsonObject);
        return jsonDataObject;
    }

}
