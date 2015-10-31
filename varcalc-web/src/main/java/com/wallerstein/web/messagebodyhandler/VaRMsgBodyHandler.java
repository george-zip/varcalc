package com.wallerstein.web.messagebodyhandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wallerstein.model.Position;
import com.wallerstein.model.VaR;
import com.wallerstein.web.gson.VaRGSONConverter;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VaRMsgBodyHandler implements MessageBodyWriter<VaR>, MessageBodyReader<VaR> {

    private GsonBuilder gsonBuilder;
    private Gson gson;

    private static final String UTF_8 = "UTF-8";

    private Gson getGson() {
        if (gsonBuilder == null) {
            gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Position.class, new VaRGSONConverter());
        }
        if (gson == null) {
            gson = gsonBuilder.create();
        }
        return gson;
    }

    public boolean isWriteable(Class<?> type, Type genericType,
                               Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    public long getSize(VaR t, Class<?> type, Type genericType, Annotation[] annotations,
                        MediaType mediaType) {
        return -1;
    }

    public boolean isReadable(Class<?> type, Type genericType,
                              Annotation[] annotations, MediaType mediaType) {
        return isWriteable(type, genericType, annotations, mediaType);
    }


    public void writeTo(VaR t, Class<?> type, Type genericType, Annotation[] annotations,
                        MediaType mediaType,
                        MultivaluedMap<String, Object> httpHeaders,
                        OutputStream entityStream)
            throws java.io.IOException, javax.ws.rs.WebApplicationException {
        try (OutputStreamWriter writer = new OutputStreamWriter(entityStream, UTF_8)) {
            getGson().toJson(t, writer);
        }
    }

    public VaR readFrom(Class<VaR> type, Type genericType,
                      Annotation[] annotations, MediaType mediaType,
                      MultivaluedMap<String, String> httpHeaders,
                      InputStream entityStream)
            throws java.io.IOException, javax.ws.rs.WebApplicationException {
        try (InputStreamReader streamReader = new InputStreamReader(entityStream, UTF_8)) {
            return getGson().fromJson(streamReader, VaR.class);
        }
    }
}
