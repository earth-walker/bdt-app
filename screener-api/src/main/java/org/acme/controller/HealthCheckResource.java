package org.acme.controller;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/health")
public class HealthCheckResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response get(@PathParam("screenerName") String screenerName) {
    return Response.ok("Success").build();

    }
}
