package ar.edu.itba.paw.webapp.form;

import org.joda.time.LocalDateTime;

import javax.validation.constraints.Future;

public class ClassReservationForm {

    private Long subjectId;

    private Long professorId;

    private Long studentId;

    @Future
    private LocalDateTime startTime;

    @Future
    private LocalDateTime endTime;

    public boolean validForm() {
        if(startTime == null || endTime == null)
            return false;

        if (startTime.compareTo(endTime) > 0){
            return false;
        }
        return true;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public Long getProfessorId() {
        return professorId;
    }

    public void setProfessorId(Long professorId) {
        this.professorId = professorId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
