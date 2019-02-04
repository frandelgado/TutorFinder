package ar.edu.itba.paw.webapp.dto;

import javax.validation.ConstraintViolation;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ErrorDTO {

    @XmlElement
    private String field;

    @XmlElement
    private String error;

    public ErrorDTO() {
    }

    public ErrorDTO(String field, String error) {
        this.field = field;
        this.error = error;
    }

    public ErrorDTO(final ConstraintViolation<?> violation) {
        this.field = violation.getPropertyPath().toString();
        this.error = violation.getMessage();
    }
}
