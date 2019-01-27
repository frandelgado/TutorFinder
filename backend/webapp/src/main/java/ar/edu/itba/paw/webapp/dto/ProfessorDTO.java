package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Professor;

import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;

@XmlRootElement
public class ProfessorDTO extends UserDTO {

    private String description;

    public ProfessorDTO(){}

    public ProfessorDTO(final Professor professor, final URI baseUri){
        super(professor.getId(), professor.getUsername(), professor.getName(),
                professor.getLastname(), professor.getEmail(), baseUri);

        this.description = professor.getDescription();
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
