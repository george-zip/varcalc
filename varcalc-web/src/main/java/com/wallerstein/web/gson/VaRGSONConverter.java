package com.wallerstein.web.gson;

import com.google.gson.*;
import com.wallerstein.model.Position;
import com.wallerstein.model.VaR;

import java.lang.reflect.Type;

public class VaRGSONConverter implements JsonSerializer<VaR>, JsonDeserializer<VaR> {

    public JsonElement serialize(final VaR src,
                                 Type typeOfSrc,
                                 JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("VaR", src.getValue());
        JsonObject jsonDataObject = new JsonObject();
        jsonDataObject.add("data", jsonObject);
        return jsonDataObject;
    }

    public VaR deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3)
            throws JsonParseException {
        JsonObject dataJson = var1.getAsJsonObject().get("data").getAsJsonObject();
        double value = dataJson.get("VaR").getAsDouble();
        return new VaR(value);
    }

}
