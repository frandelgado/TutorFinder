package ar.edu.itba.paw.models;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "courses")
@IdClass(CourseID.class)
public class Course {

    @Id
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="user_id", foreignKey = @ForeignKey(name = "courses_user_id_fkey"))
    private Professor professor;

    @Id
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="subject_id", foreignKey = @ForeignKey(name = "courses_subject_id_fkey"))
    private Subject subject;

    @Column(length = 512, nullable = false)
    private String description;

    @Column(nullable = false)
    private Double price;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "course", orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Comment> comments;

    @Formula("(SELECT AVG(c.rating) FROM comments c WHERE c.course_user_id = user_id AND" +
            " c.course_subject_id = subject_id)")
    private Double rating;

    public Course(){}

    public Course(Professor professor, Subject subject, String description, Double price) {
        this.professor = professor;
        this.subject = subject;
        this.description = description;
        this.price = price;
    }

    public Professor getProfessor() {
        return professor;
    }

    public Subject getSubject() {
        return subject;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public List<Comment> getComments() {
        return comments;
    }
    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Double getRating() {
        return rating;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(professor, course.professor) &&
                Objects.equals(subject, course.subject) &&
                Objects.equals(description, course.description) &&
                Objects.equals(price, course.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(professor, subject, description, price);
    }
}
