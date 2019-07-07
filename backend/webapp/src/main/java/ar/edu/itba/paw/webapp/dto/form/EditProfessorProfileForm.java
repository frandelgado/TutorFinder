package ar.edu.itba.paw.webapp.dto.form;

import ar.edu.itba.paw.webapp.validator.FileType;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.validation.constraints.Size;

public class EditProfessorProfileForm {

    @Size(min = 50, max = 300, message = "{Size.editProfessorProfileForm.description}")
    @FormDataParam("description")
    private String description;

//    @FileSize(max = 81920)
    @FileType(message = "{FileType.editProfessorProfileForm.description}")
    @FormDataParam("picture")
    private FormDataBodyPart picture;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FormDataBodyPart getPicture() {
        return picture;
    }

    public void setPicture(FormDataBodyPart picture) {
        this.picture = picture;
    }
}
