package ar.edu.itba.paw.webapp.exceptionmapper;

import ar.edu.itba.paw.exceptions.DTOConstraintException;
import ar.edu.itba.paw.webapp.dto.ValidationErrorDTO;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ConstraintMapper implements ExceptionMapper<DTOConstraintException> {

    @Override
    public Response toResponse(final DTOConstraintException exception) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(new ValidationErrorDTO(exception.getViolations()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
