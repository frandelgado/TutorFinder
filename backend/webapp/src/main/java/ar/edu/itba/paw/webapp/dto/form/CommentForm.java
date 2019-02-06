package ar.edu.itba.paw.webapp.dto.form;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CommentForm {

    @NotNull
    @Size(min = 1, max = 1024)
    private String commentBody;

    @Min(1)
    @Max(5)
    private Integer rating;

    public String getCommentBody() {
        return commentBody;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
