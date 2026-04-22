package theshara.smartcampusapi.exceptions;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class SensorUnavailableMapper implements ExceptionMapper<SensorUnavailableException> {
    @Override
    public Response toResponse(SensorUnavailableException ex) {
        return Response.status(Response.Status.FORBIDDEN) // 403 Forbidden
                .entity("{\"error\":\"Forbidden\", \"message\":\"" + ex.getMessage() + "\"}")
                .type(MediaType.APPLICATION_JSON).build();
    }
}