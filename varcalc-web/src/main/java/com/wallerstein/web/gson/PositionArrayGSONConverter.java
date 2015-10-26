package com.wallerstein.web.gson;

import com.google.gson.*;
import com.wallerstein.model.Position;

import java.lang.reflect.Type;

public class PositionArrayGSONConverter implements JsonSerializer<Position[]>,
        JsonDeserializer<Position[]> {

    private PositionGSONConverter PositionGSONConverter = new PositionGSONConverter();

    @Override
    public Position[] deserialize(JsonElement json, Type typeOfT,
                                   JsonDeserializationContext context) throws JsonParseException {
        JsonArray jArray = json.getAsJsonArray();
        Position[] pArray = new Position[jArray.size()];
        int index = 0;
        for(JsonElement jsonE : jArray) {
            Position tmp = PositionGSONConverter.deserialize(jsonE, Position.class, context);
            pArray[index++] = tmp;
        }
        return pArray;
    }

    @Override
    public JsonElement serialize(final Position[] src,
                                 Type typeOfSrc,
                                 JsonSerializationContext context){
        JsonArray jsonArray = new JsonArray();
        for(Position Position : src) {
            JsonElement tmp = PositionGSONConverter.serialize(Position, Position.class, context);
            jsonArray.add(tmp);
        }
        return jsonArray;
    }

}
