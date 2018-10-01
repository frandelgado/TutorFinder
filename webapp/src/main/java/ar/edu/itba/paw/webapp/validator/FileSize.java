package ar.edu.itba.paw.webapp.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FileSizeValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface FileSize {
    String message() default "El tamaño del archivo debe tener un tamaño maximo de 80KB";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
