package org.acme.controller;

import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.mapper.ScreenerMapper;
import org.acme.model.Screener;
import org.acme.repository.ScreenerRepository;

import java.util.List;
import java.util.Optional;

@Path("/api/{userId}")
public class ScreenerResource {

    @Inject
    ScreenerRepository screenerRepository;

    @GET
    @Path("/screeners")
    public Response getScreeners(@PathParam("userId") String userId) {
        Log.info("Fetching screeners for user: " + userId);
        //perform authentication
        List<Screener> screeners = screenerRepository.getScreeners(userId);


        return Response.ok(screeners, MediaType.APPLICATION_JSON).build();
    }


    @GET
    @Path("/screener/{screenerId}")
    public Response getScreener(@PathParam("userId") String userId, @PathParam("screenerId") String screenerId) {
        Log.info("Fetching screener " + screenerId + "  for user " + userId);

        //perform authentication

        Optional<Screener> screenerOptional = screenerRepository.getScreener(screenerId);

        if (screenerOptional.isEmpty()){
          throw new NotFoundException();
        }
        //check that user is authorized to view screener before returning

        Screener screener = screenerOptional.get();
        return Response.ok(screener, MediaType.APPLICATION_JSON).build();
    }
}
