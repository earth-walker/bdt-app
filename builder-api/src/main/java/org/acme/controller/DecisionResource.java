package org.acme.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.model.domain.Screener;
import org.acme.persistence.ScreenerRepository;
import org.acme.persistence.StorageService;
import org.acme.service.DmnParser;
import org.acme.service.DmnEvaluationService;

import java.io.InputStream;
import java.time.Instant;
import java.util.*;

@Path("/api/decision")
public class DecisionResource {

    @Inject
    ScreenerRepository screenerRepository;

    @Inject
    StorageService storageService;

    @Inject
    DmnEvaluationService dmnEvaluationService;

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

        Optional<Screener> screenerOptional = screenerRepository.getScreener(screenerId);

        String notFoundResponseMessage = String.format("Form %s was not found", screenerId);
        if (screenerOptional.isEmpty()){
            throw new NotFoundException(notFoundResponseMessage);
        }

        Screener screener = screenerOptional.get();

        Map<String, Object> result;

        try {
        if (screener.getLastDmnCompile() == null || Instant.parse(screener.getLastDmnSave()).isAfter(Instant.parse(screener.getLastDmnCompile()))){
            //compile working dmn
            String dmnXml = dmnEvaluationService.compileWorkingDmnModel(screenerId);
            updateScreenerLastCompileTime(screenerId, dmnXml);
        }

        String filePath = storageService.getScreenerWorkingDmnModelPath(screenerId);
        Optional<InputStream> dmnDataOpt = storageService.getFileInputStreamFromStorage(filePath);

        if (dmnDataOpt.isEmpty()){
            throw new NotFoundException();
        }

        result = dmnEvaluationService.evaluateDecision(screener, inputData);

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

    private void updateScreenerLastCompileTime(String screenerId, String dmnXml) throws Exception {
        Screener updateScreener = new Screener();
        updateScreener.setId(screenerId);
        updateScreener.setLastDmnCompile(Instant.now().toString());
        DmnParser dmnParser = new DmnParser(dmnXml);
        updateScreener.setWorkingDmnName(dmnParser.getName());
        updateScreener.setWorkingDmnNameSpace(dmnParser.getNameSpace());
        screenerRepository.updateScreener(updateScreener);
    }
}