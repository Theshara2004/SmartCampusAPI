package theshara.smartcampusapi.exceptions;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

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