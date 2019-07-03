package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Comment;

import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

@XmlRootElement
public class CommentListDTO {

    private List<CommentDTO> comments;
    private long totalCount;
    private int count;

    public CommentListDTO() {
    }

    public CommentListDTO(final List<Comment> comments, final long totalCount, final URI uri) {
        this.comments = new LinkedList<>();
        comments.forEach(comment -> this.comments.add(new CommentDTO(comment, uri)));
        this.count = comments.size();
        this.totalCount = totalCount;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
