package com.wallerstein.web.gson;

import com.google.gson.*;
import com.wallerstein.model.Position;

import java.lang.reflect.Type;

public class PositionGSONConverter implements JsonSerializer<Position>, JsonDeserializer<Position> {

    public JsonElement serialize(final Position src,
                                 Type typeOfSrc,
                                 JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("symbol", src.getSecurityID());
        jsonObject.addProperty("quantity", src.getQuantity());
        jsonObject.addProperty("side", src.getSide().toString());
        JsonObject jsonDataObject = new JsonObject();
        jsonDataObject.add("data", jsonObject);
        return jsonDataObject;
    }

    public Position deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3)
            throws JsonParseException {
        JsonObject dataJson = var1.getAsJsonObject().get("data").getAsJsonObject();
        double qty = dataJson.get("quantity").getAsDouble();
        String symbol = dataJson.get("symbol").getAsString();
        return new Position(symbol, qty);
    }

}
