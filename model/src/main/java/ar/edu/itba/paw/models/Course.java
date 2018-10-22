package ar.edu.itba.paw.models;

import javax.persistence.*;

@Entity
@Table(name = "courses")
@IdClass(CourseID.class)
public class Course {

    //TODO: no estoy seguro si el schema refleja que el profesor no deberia ser nullable.
    @Id
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="user_id")
    private Professor professor;

    @Id
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="subject_id")
    private Subject subject;

    @Column(length = 512, nullable = false)
    private String description;

    @Column(nullable = false)
    private Double price;

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
}
