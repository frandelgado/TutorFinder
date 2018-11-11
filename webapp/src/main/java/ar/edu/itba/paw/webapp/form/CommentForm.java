package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CommentForm {

    @NotNull
    @Size(min = 1, max = 1024)
    private String commentBody;

    private Long commentSubjectId;

    private Long commentProfessorId;

    @Min(1)
    @Max(5)
    private Integer rating;

    public String getCommentBody() {
        return commentBody;
    }

    public void setCommentBody(String body) {
        this.commentBody = body;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Long getCommentSubjectId() {
        return commentSubjectId;
    }

    public void setCommentSubjectId(Long commentSubjectId) {
        this.commentSubjectId = commentSubjectId;
    }

    public Long getCommentProfessorId() {
        return commentProfessorId;
    }

    public void setCommentProfessorId(Long commentProfessorId) {
        this.commentProfessorId = commentProfessorId;
    }
}
