package com.wallerstein.web;

import com.wallerstein.model.Portfolio;
import com.wallerstein.model.Position;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;

@Path("portfolios")
public class PortfoliosService {

    private Portfolio portfolio = null;

    public PortfoliosService() {
        List<Position> positionList = new ArrayList<Position>();
        positionList.add(new Position("XYZ", 100));
        positionList.add(new Position("ZZZ", 50));
        portfolio = new Portfolio(positionList);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Position get() {
        return new Position("IBM", 100);
    }
}
