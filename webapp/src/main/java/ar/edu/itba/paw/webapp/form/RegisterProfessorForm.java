package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RegisterProfessorForm {

    @NotNull
    @Size(min = 50, max = 300)
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
