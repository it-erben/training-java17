package tech.erben.assignments.fulfillment.rest;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class WebAppExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(WebApplicationException exception) {
        Response.StatusType status = exception.getResponse().getStatusInfo();
        ApiError error = new ApiError(exception.getMessage(), currentPath(), status.getStatusCode());
        return Response.status(status).entity(error).build();
    }

    private String currentPath() {
        return uriInfo != null ? uriInfo.getPath() : "";
    }
}
