package tech.erben.assignments.fulfillment.rest;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Exception> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(Exception exception) {
        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        ApiError error = new ApiError(exception.getMessage(), currentPath(), status.getStatusCode());
        return Response.status(status).entity(error).build();
    }

    private String currentPath() {
        return uriInfo != null ? uriInfo.getPath() : "";
    }
}
