package org.acme.controller;

import io.quarkus.logging.Log;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.auth.AuthUtils;
import org.acme.model.Screener;

import java.util.List;

@Path("/api")
public class DmnModelResource {


//    @GET
//    @Path("/dmn-models")
//    public Response getScreeners(@Context ContainerRequestContext requestContext) {
//        String userId = AuthUtils.getUserId(requestContext);
//        if (userId == null){
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        }
//        Log.info("Get dmn models list");
//        List<Screener> screeners = screenerRepository.getScreeners(userId);
//
//        return Response.ok(screeners, MediaType.APPLICATION_JSON).build();
//    }
}
