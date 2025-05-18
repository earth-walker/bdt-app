package org.acme.controller;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.inject.Inject;

import jakarta.ws.rs.core.Response;
import org.acme.model.Screener;
import org.acme.repository.ScreenerRepository;

import java.util.Optional;

@Path("/api/screener/{screenerName}")
public class ScreenerResource {

    @Inject
    ScreenerRepository screenerRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("screenerName") String screenerName) {

        Optional<Screener> screenerOptional = screenerRepository.getScreenerByName(screenerName);

        String notFoundResponseMessage = String.format("Form %s was not found", screenerName);
        if (screenerOptional.isEmpty()){
            throw new NotFoundException(notFoundResponseMessage);
        }

        Screener screener = screenerOptional.get();
        if (screener.getFormModel().isEmpty() || !screener.isPublished()) {
            throw new NotFoundException(notFoundResponseMessage);
        }

        return Response.ok(screener, MediaType.APPLICATION_JSON).build();
    }
}