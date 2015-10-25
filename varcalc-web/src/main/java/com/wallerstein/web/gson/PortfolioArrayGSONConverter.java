package com.wallerstein.web.gson;

import com.google.gson.*;
import com.wallerstein.model.Portfolio;

import java.lang.reflect.Type;

public class PortfolioArrayGSONConverter implements JsonSerializer<Portfolio[]>,
        JsonDeserializer<Portfolio[]> {

    private PortfolioGSONConverter portfolioGSONConverter = new PortfolioGSONConverter();

    @Override
    public Portfolio[] deserialize(JsonElement json, Type typeOfT,
                             JsonDeserializationContext context) throws JsonParseException {
        JsonArray jArray = json.getAsJsonArray();
        Portfolio[] pArray = new Portfolio[jArray.size()];
        int index = 0;
        for(JsonElement jsonE : jArray) {
            Portfolio tmp = portfolioGSONConverter.deserialize(jsonE, Portfolio.class, context);
            pArray[index++] = tmp;
        }
        return pArray;
    }

    @Override
    public JsonElement serialize(final Portfolio[] src,
                                 Type typeOfSrc,
                                 JsonSerializationContext context){
        JsonArray jsonArray = new JsonArray();
        for(Portfolio portfolio : src) {
            JsonElement tmp = portfolioGSONConverter.serialize(portfolio, Portfolio.class, context);
            jsonArray.add(tmp);
        }
        return jsonArray;
    }

}
