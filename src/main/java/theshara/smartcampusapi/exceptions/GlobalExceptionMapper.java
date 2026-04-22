package theshara.smartcampusapi.exceptions;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable ex) {
        ex.printStackTrace();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR) // 500
                .entity("{\"error\":\"Internal Server Error\", \"message\":\"An unexpected error occurred.\"}")
                .type(MediaType.APPLICATION_JSON).build();
        
    }
}