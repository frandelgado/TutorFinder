package ar.edu.itba.paw.webapp.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FileTypeValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface FileType {
    String message() default "El archivo debe estar en formato jpeg";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
