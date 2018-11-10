package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validator.FileSize;
import ar.edu.itba.paw.webapp.validator.FileType;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;

public class EditProfessorProfileForm {

    @Size(min = 50, max = 300)
    private String description;

    @FileSize
    @FileType
    private MultipartFile picture;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile getPicture() {
        return picture;
    }

    public void setPicture(MultipartFile picture) {
        this.picture = picture;
    }
}
