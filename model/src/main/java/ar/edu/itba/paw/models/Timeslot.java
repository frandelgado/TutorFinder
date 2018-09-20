package ar.edu.itba.paw.models;

public class Timeslot {
    private final Integer day;
    private final Integer hour;

    public Timeslot(Integer day, Integer hour) {
        this.day = day;
        this.hour = hour;
    }

    public Integer getDay() {
        return day;
    }

    public Integer getHour() {
        return hour;
    }
}
