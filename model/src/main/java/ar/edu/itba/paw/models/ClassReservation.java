package ar.edu.itba.paw.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "course_requests")
public class ClassReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "course_requests_id_seq")
    @SequenceGenerator(sequenceName = "course_requests_id_seq", name = "course_requests_id_seq",  allocationSize = 1)
    @Column(name = "id")
    private Long classRequestId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User student;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Course course;

    @Column(nullable = false)
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime startTime;

    @Column(nullable = false)
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime endTime;

    //Status is 0 if approved, 1 if denied, 2 if yet undefined;
    @Column(name = "status", nullable = false)
    private Integer status;

    private String comment;

    ClassReservation(){}


    public ClassReservation confirm(String comment) {
        this.comment = comment;
        this.status = 0;
        return this;
    }

    public ClassReservation deny(String comment) {
        this.comment = comment;
        this.status = 2;
        return this;
    }

    public ClassReservation(User student, Course course, LocalDateTime startTime, LocalDateTime endTime, Integer status, String comment) {
        this.student = student;
        this.course = course;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassReservation that = (ClassReservation) o;
        return Objects.equals(classRequestId, that.classRequestId) &&
                Objects.equals(student, that.student) &&
                Objects.equals(course, that.course) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(endTime, that.endTime) &&
                Objects.equals(status, that.status) &&
                Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classRequestId, student, course, startTime, endTime, status, comment);
    }
}
