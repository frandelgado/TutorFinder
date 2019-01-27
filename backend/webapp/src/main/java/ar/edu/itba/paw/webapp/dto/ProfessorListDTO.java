package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Professor;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

public class ProfessorListDTO {

    private List<ProfessorDTO> professors;
    private int count;
    private int totalCount;

    public ProfessorListDTO() {}

    public ProfessorListDTO(final List<Professor> professors, final URI baseUri) {
        this.professors = new LinkedList<>();
        for(Professor professor : professors) {
            this.professors.add(new ProfessorDTO(professor, baseUri));
        }
        this.count = professors.size();
    }

    public List<ProfessorDTO> getProfessors() {
        return professors;
    }

    public void setProfessors(List<ProfessorDTO> professors) {
        this.professors = professors;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
