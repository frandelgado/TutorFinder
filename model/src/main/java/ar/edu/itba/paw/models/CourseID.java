package ar.edu.itba.paw.models;

import java.io.Serializable;
import java.util.Objects;

public class CourseID implements Serializable {

    protected Professor professor;
    protected Subject subject;

    public CourseID(){}

    public CourseID(Professor professor, Subject subject){
        this.professor = professor;
        this.subject = subject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseID courseID = (CourseID) o;
        return Objects.equals(professor.getId(), courseID.professor.getId()) &&
                Objects.equals(subject.getId(), courseID.subject.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(professor.getId(), subject.getId());
    }
}
