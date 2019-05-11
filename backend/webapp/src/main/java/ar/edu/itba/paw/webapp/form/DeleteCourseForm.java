package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotNull;

public class DeleteCourseForm {

    @NotNull
    private Long subject;

    public Long getSubject() {
        return subject;
    }

    public void setSubject(Long subjectId) {
        this.subject = subjectId;
    }

}
