package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ContactForm {

    @NotNull
    @Size(min = 1, max = 128)
    private String subject;

    @NotNull
    @Size(min = 1, max = 512)
    private String body;
}
