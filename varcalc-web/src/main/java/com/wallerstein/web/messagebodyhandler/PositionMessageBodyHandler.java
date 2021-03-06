package com.wallerstein.web.messagebodyhandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wallerstein.model.Position;
import com.wallerstein.web.gson.PositionGSONConverter;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PositionMessageBodyHandler implements MessageBodyWriter<Position>, MessageBodyReader<Position> {

    private GsonBuilder gsonBuilder;
    private Gson gson;

    private static final String UTF_8 = "UTF-8";

    private Gson getGson() {
        if (gsonBuilder == null) {
            gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Position.class, new PositionGSONConverter());
        }
        if (gson == null) {
            gson = gsonBuilder.create();
        }
        return gson;
    }

    public boolean isWriteable(Class<?> type,
                               Type genericType,
                               Annotation[] annotations,
                               MediaType mediaType) {
        return type == Position.class;
    }

    public boolean isReadable(Class<?> type, Type genericType,
                              Annotation[] annotations, MediaType mediaType) {
        return isWriteable(type, genericType, annotations, mediaType);
    }

    public long getSize(Position t,
                        Class<?> type,
                        Type genericType,
                        Annotation[] annotations,
                        MediaType mediaType) {
        return -1;
    }

    public void writeTo(Position position,
                        Class<?> type,
                        Type genericType,
                        Annotation[] annotations,
                        MediaType mediaType,
                        MultivaluedMap<String, Object> httpHeaders,
                        OutputStream entityStream)
            throws IOException,
            WebApplicationException {
        try (OutputStreamWriter writer = new OutputStreamWriter(entityStream, UTF_8)) {
            getGson().toJson(position, writer);
        }
    }

    public Position readFrom(Class<Position> type, Type genericType,
                             Annotation[] annotations, MediaType mediaType,
                             MultivaluedMap<String, String> httpHeaders,
                             InputStream entityStream) throws java.io.IOException, javax.ws.rs.WebApplicationException {
        try (InputStreamReader streamReader = new InputStreamReader(entityStream, UTF_8)) {
            return getGson().fromJson(streamReader, Position.class);
        }
    }
}
