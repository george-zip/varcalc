package com.wallerstein.web.messagebodyhandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wallerstein.model.Portfolio;
import com.wallerstein.web.gson.PortfolioGSONConverter;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
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
public class PortfolioMessageBodyHandler implements MessageBodyWriter<Portfolio>, MessageBodyReader<Portfolio> {

    private GsonBuilder gsonBuilder;
    private Gson gson;

    private static final String UTF_8 = "UTF-8";

    private Gson getGson() {
        if(gsonBuilder == null) {
            gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Portfolio.class, new PortfolioGSONConverter());
        }
        if(gson == null) {
            gson = gsonBuilder.create();
        }
        return gson;
    }

    @Override
    public boolean isWriteable(Class<?> type,
                               Type genericType,
                               Annotation[] annotations,
                               MediaType mediaType) {
        return type == Portfolio.class;
    }

    @Override
    public long getSize(Portfolio t, Class<?> type, Type genericType, Annotation[] annotations,
                        MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(Portfolio portfolio,
                        Class<?> type,
                        Type genericType,
                        Annotation[] annotations,
                        MediaType mediaType,
                        MultivaluedMap<String,Object> httpHeaders,
                        OutputStream entityStream)
            throws IOException,
            WebApplicationException {
        OutputStreamWriter writer = new OutputStreamWriter(entityStream, UTF_8);
        try {
            getGson().toJson(portfolio, writer);
        }
        finally {
            writer.close();
        }
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations,
                              MediaType mediaType) {
        return genericType == Portfolio.class;
    }

    @Override
    public Portfolio readFrom(Class<Portfolio> type, Type genericType,
                              Annotation[] annotations, MediaType mediaType,
                              MultivaluedMap<String, String> httpHeaders,
                              InputStream entityStream) throws IOException, WebApplicationException {
        InputStreamReader streamReader = new InputStreamReader(entityStream, UTF_8);
        try {
            return getGson().fromJson(streamReader, Portfolio.class);
        }
        finally {
            streamReader.close();
        }
    }

}