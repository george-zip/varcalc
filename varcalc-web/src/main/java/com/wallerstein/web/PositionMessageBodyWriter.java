package com.wallerstein.web;

import com.wallerstein.model.Position;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class PositionMessageBodyWriter implements MessageBodyWriter<Position> {

    @Override
    public boolean isWriteable(Class<?> type,
                               Type genericType,
                               Annotation[] annotations,
                               MediaType mediaType) {
        return type == Position.class;
    }

    @Override
    public long getSize(Position t,
                        Class<?> type,
                        Type genericType,
                        Annotation[] annotations,
                        MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(Position position,
                        Class<?> type,
                        Type genericType,
                        Annotation[] annotations,
                        MediaType mediaType,
                        MultivaluedMap<String,Object> httpHeaders,
                        OutputStream entityStream)
            throws IOException,
            WebApplicationException {
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("symbol", position.getSecurityID())
                .add("quantity", position.getQuantity())
                .build();
        String s = jsonObject.toString();
        byte[] bytes = s.getBytes();
        for (int i = 0; i < bytes.length; i++) {
            entityStream.write(bytes[i]);
        }
    }
}
