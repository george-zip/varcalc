package com.wallerstein.web.gson;

import com.google.gson.*;
import com.wallerstein.model.Portfolio;
import com.wallerstein.model.Position;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PortfolioGSONConverter implements JsonSerializer<Portfolio>, JsonDeserializer<Portfolio> {

    public JsonElement serialize(final Portfolio src,
                                 Type typeOfSrc,
                                 JsonSerializationContext context){
        JsonObject jsonObject= new JsonObject();
        jsonObject.addProperty("id", src.getID());
        jsonObject.addProperty("name", src.getName());
        JsonObject jsonDataObject= new JsonObject();
        jsonDataObject.add("data", jsonObject);
        return jsonDataObject;
    }

    public Portfolio deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3)
            throws JsonParseException {
        JsonObject dataJson = var1.getAsJsonObject().get("data").getAsJsonObject();
        String name = dataJson.get("name").getAsString();
        return new Portfolio(new ArrayList<Position>(), name);
    }

}
