package theshara.smartcampusapi.exceptions;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class LinkedResourceNotFoundMapper implements ExceptionMapper<LinkedResourceNotFoundException> {
    @Override
    public Response toResponse(LinkedResourceNotFoundException ex) {
        // 422 Unprocessable Entity
        return Response.status(422)
                .entity("{\"error\":\"Unprocessable Entity\", \"message\":\"" + ex.getMessage() + "\"}")
                .type(MediaType.APPLICATION_JSON).build();
    }
}