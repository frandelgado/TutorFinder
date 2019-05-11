package ar.edu.itba.paw.exceptions;

import javax.validation.ConstraintViolation;
import java.util.Set;

public class DTOConstraintException extends Exception{

    private final Set<? extends ConstraintViolation<?>> violations;

    public DTOConstraintException(final Set<? extends ConstraintViolation<?>> constraintViolations) {
        this.violations = constraintViolations;
    }

    public Set<? extends ConstraintViolation<?>> getViolations() {
        return violations;
    }
}
