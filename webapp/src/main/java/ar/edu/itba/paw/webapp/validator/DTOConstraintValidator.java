package ar.edu.itba.paw.webapp.validator;

import ar.edu.itba.paw.exceptions.DTOConstraintException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@Component
public class DTOConstraintValidator {

    @Autowired
    private Validator validator;

    public <T> void validate(T dto) throws DTOConstraintException {
        final Set<ConstraintViolation<T>> violations = validator.validate(dto);

        if (!violations.isEmpty())
            throw new DTOConstraintException(violations);
    }
}
