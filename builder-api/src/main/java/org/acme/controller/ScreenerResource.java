package org.acme.controller;

import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.model.Screener;
import org.acme.repository.ScreenerRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Path("/api")
public class ScreenerResource {

    @Inject
    ScreenerRepository screenerRepository;

    @GET
    @Path("/screeners")
    public Response getScreeners() {
        String userId = "TEST";
        Log.info("Fetching screeners for user: " + userId);
        //perform authentication
        List<Screener> screeners = screenerRepository.getScreeners(userId);


        return Response.ok(screeners, MediaType.APPLICATION_JSON).build();
    }


    @GET
    @Path("/screener/{screenerId}")
    public Response getScreener(@PathParam("screenerId") String screenerId) {

        String userId = "TEST";
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


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/screener")
    public Response postScreener(Screener newScreener){
        String userId = "TEST";
        //Perform authentication

        //initialize screener data not in form
        newScreener.setIsPublished(false);
        newScreener.setOwnerId("TEST");
        try {
            String screenerId = screenerRepository.saveNewScreener(newScreener);
            newScreener.setId(screenerId);
            return Response.ok(newScreener, MediaType.APPLICATION_JSON).build();
        } catch (Exception e){
            return  Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Could not save Screener"))
                    .build();
        }
    }


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/screener")
    public Response updateScreener(Screener screener){
        String userId = "TEST";
        //Perform authentication

        //add user info to the update data
        screener.setOwnerId("TEST");

        Log.info("isPublished: " + screener.isPublished());
        try {
            screenerRepository.updateScreener(screener);

            return Response.ok().build();
        } catch (Exception e){
            return  Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Could not update Screener"))
                    .build();
        }
    }


}
