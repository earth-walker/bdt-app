package org.acme.controller;

import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.auth.AuthUtils;
import org.acme.model.domain.DmnModel;
import org.acme.persistence.DmnModelRepository;

import java.util.List;

@Path("/api")
public class DmnModelResource {

    @Inject
    private DmnModelRepository dmnModelRepository;

    @GET
    @Path("/dmn-models")
    public Response getDmnModels(@Context ContainerRequestContext requestContext) {
        String userId = AuthUtils.getUserId(requestContext);
        if (userId == null){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        Log.info("Get dmn models list");
        List<DmnModel> dmnModels = dmnModelRepository.getAllDmnModels();

        return Response.ok(dmnModels, MediaType.APPLICATION_JSON).build();
    }
}
