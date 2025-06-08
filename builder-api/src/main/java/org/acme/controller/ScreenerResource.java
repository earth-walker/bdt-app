package org.acme.controller;

import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.model.Screener;
import org.acme.repository.ScreenerRepository;
import org.acme.repository.utils.StorageUtils;

import java.util.HashMap;
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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/save-form-schema")
    public Response saveFormSchema(@QueryParam("screenerId") String screenerId, String content){
        if (screenerId == null || screenerId.isBlank()){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error: Missing required query parameter: screenerId")
                    .build();
        }
        try {

            // perform authorization for screener

            String filePath = StorageUtils.getScreenerWorkingFormSchemaPath(screenerId);
            StorageUtils.writeStringToStorage(filePath, content, "application/json");
            Log.info("Saved form schema of screener " + screenerId + " to storage");
            return Response.ok().build();
        } catch (Exception e){
            Log.info(("Failed to save form for screener " + screenerId));
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Path("/save-dmn-model")
    public Response saveDmnModel(@QueryParam("screenerId") String screenerId, String content){
        if (screenerId == null || screenerId.isBlank()){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error: Missing required query parameter: screenerId")
                    .build();
        }
        try {

            // perform authorization for screener

            String filePath = StorageUtils.getScreenerWorkingDmnModelPath(screenerId);
            StorageUtils.writeStringToStorage(filePath, content, "application/xml");
            Log.info("Saved DMN model of screener " + screenerId + " to storage");
            return Response.ok().build();
        } catch (Exception e){
            Log.info(("Failed to save DMN model for screener " + screenerId));
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Path("/publish")
    public Response publishScreener(@QueryParam("screenerId") String screenerId){
        if (screenerId == null || screenerId.isBlank()){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error: Missing required query parameter: screenerId")
                    .build();
        }
        try {
            // Perf authorization to publish screener
            Screener updateScreener = new Screener();
            updateScreener.setId(screenerId);
            updateScreener.setIsPublished(true);
            screenerRepository.updateScreener(updateScreener);
            Log.info("Updated Screener " + screenerId + " to published.");
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("screenerUrl", getScreenerUrl(screenerId));
            return Response.ok().entity(responseData).build();

        } catch (Exception e){
            Log.error("Error: Error updating screener to published. Screener: " + screenerId);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    private static String getScreenerUrl(String screenerId) {
        return "screener/" + screenerId;
    }

    @POST
    @Path("/unpublish")
    public Response unpublishScreener(@QueryParam("screenerId") String screenerId){
        if (screenerId == null || screenerId.isBlank()){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error: Missing required query parameter: screenerId")
                    .build();
        }
        try {
            // Perf authorization to publish screener
            Screener updateScreener = new Screener();
            updateScreener.setId(screenerId);
            updateScreener.setIsPublished(false);
            screenerRepository.updateScreener(updateScreener);
            Log.info("Updated Screener " + screenerId + " to unpublished.");
            return Response.ok().build();

        } catch (Exception e){
            Log.error("Error: Error updating screener to unpublished. Screener: " + screenerId);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DELETE
    @Path("/screener/delete")
    public Response deleteScreener(@QueryParam("screenerId") String screenerId){
        if (screenerId == null || screenerId.isBlank()){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error: Missing required query parameter: screenerId")
                    .build();
        }
        try {
            //AUTHORIZE delete

            screenerRepository.deleteScreener(screenerId);
            return Response.ok().build();
        } catch (Exception e){
            Log.error("Error: error deleting screener " + screenerId);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
