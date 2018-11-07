package ar.edu.itba.paw.models;

import javax.persistence.*;

@Entity
@Table(name = "schedules")
public class Timeslot {
    //TODO construir primary key compuesta por todos sus atributos

    @Column(nullable = false)
    private Integer day;

    @Column(nullable = false)
    private Integer hour;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="user_id")
    private Professor professor;

    public Timeslot(Integer day, Integer hour, Professor professor) {
        this.day = day;
        this.hour = hour;
        this.professor = professor;
    }

    public Timeslot(){};

    public Integer getDay() {
        return day;
    }

    public Integer getHour() {
        return hour;
    }
}
