package org.acme.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.repository.ScreenerRepository;
import org.acme.repository.utils.StorageUtils;
import org.acme.service.DmnService;

import java.io.InputStream;
import java.util.*;

@Path("/api/decision")
public class DecisionResource {

    @Inject
    ScreenerRepository screenerRepository;

    @Inject
    DmnService dmnService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@QueryParam("screenerId") String screenerId, Map<String, Object> inputData) {
        if (screenerId == null || screenerId.isBlank()){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error: Missing required query parameter: screenerId")
                    .build();
        }

        if (inputData == null || inputData.isEmpty()){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error: Missing decision inputs")
                    .build();
        }

        String filePath = StorageUtils.getScreenerWorkingDmnModelPath(screenerId);
        Optional<InputStream> dmnDataOpt = StorageUtils.getFileInputStreamFromStorage(filePath);

        if (dmnDataOpt.isEmpty()){
            throw new NotFoundException();
        }

        InputStream dmnFileInputStream = dmnDataOpt.get();
        List<Map<String, Object>> result;
        try {
            result = dmnService.evaluateDecision(dmnFileInputStream, inputData);
        } catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        if (!result.isEmpty()){
            return Response.ok().entity(result).build();
        }

        else {
            return Response.ok().entity(Collections.emptyList()).build();
        }
    }
}