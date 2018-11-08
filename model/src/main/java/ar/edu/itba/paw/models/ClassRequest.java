package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "course_requests")
public class ClassRequest {

    @Id
    private Integer classRequestId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @Column(name = "student_id")
    private User student;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @Column(name = "professor_id")
    private Professor professor;

    @Column(name = "day")
    private Integer day;

    @Column(name = "start_hour")
    private Integer startHour;

    @Column(name = "end_hour")
    private Integer endHour;

    //Status is 0 if approved, 1 if denied, 2 if yet undefined;
    @Column(name = "status")
    private Integer status;

    ClassRequest(){};

    public ClassRequest(User student, Professor professor, Integer day, Integer startHour,
                        Integer endHour, Integer status) {
        this.student = student;
        this.professor = professor;
        this.day = day;
        this.startHour = startHour;
        this.endHour = endHour;
        this.status = status;
    }

    public ClassRequest confirm() {
        this.status = 0;
        return this;
    }

    public ClassRequest deny() {
        this.status = 2;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassRequest that = (ClassRequest) o;
        return Objects.equals(classRequestId, that.classRequestId) &&
                Objects.equals(student, that.student) &&
                Objects.equals(professor, that.professor) &&
                Objects.equals(day, that.day) &&
                Objects.equals(startHour, that.startHour) &&
                Objects.equals(endHour, that.endHour) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classRequestId, student, professor, day, startHour, endHour, status);
    }

}
