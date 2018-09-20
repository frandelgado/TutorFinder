package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ContactForm {

    @NotNull
    @Size(min = 1, max = 128)
    private String messageSubject;

    @NotNull
    @Size(min = 1, max = 512)
    private String body;

    public String getMessageSubject() {
        return messageSubject;
    }

    public void setMessageSubject(String subject) {
        this.messageSubject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
