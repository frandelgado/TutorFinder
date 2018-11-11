package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "courses")
@IdClass(CourseID.class)
public class Course {

    //TODO: no estoy seguro si el schema refleja que el profesor no deberia ser nullable.
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

    @OneToMany(fetch = FetchType.LAZY)
    private List<ClassReservation> classReservations;

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
