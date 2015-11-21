package com.wallerstein.web.filter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@PreMatching
public class RESTRequestFilter implements ContainerRequestFilter {

    @Override
    public void filter( ContainerRequestContext requestCtx ) throws IOException {
        requestCtx.getHeaders().add( "Access-Control-Allow-Credentials", "true" );
        //requestCtx.getHeaders().add( "Access-Control-Allow-Origin", "*");
        requestCtx.getHeaders().add( "Access-Control-Allow-Methods", "OPTIONS, GET, POST, DELETE, PUT" );
        requestCtx.getHeaders().add( "Access-Control-Allow-Headers", "Content-Type" );

        // When HttpMethod comes as OPTIONS, just acknowledge that it accepts...
        if ( requestCtx.getRequest().getMethod().equals( "OPTIONS" ) ) {
            System.out.println("HTTP Method (OPTIONS) - Detected!");

            // Just send a OK signal back to the browser (Abort the filter chain with a response.)
            Response response = Response.status( Response.Status.OK )
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                    //.header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "Content-Type, accept, headers").build();

            requestCtx.abortWith( response );
        }

    }
}
