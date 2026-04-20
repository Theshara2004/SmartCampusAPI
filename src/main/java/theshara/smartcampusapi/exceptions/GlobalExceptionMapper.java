package theshara.smartcampusapi.exceptions;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable ex) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR) // 500
                .entity("{\"error\":\"Internal Server Error\", \"message\":\"An unexpected error occurred.\"}")
                .type(MediaType.APPLICATION_JSON).build();
    }
}