package ar.edu.itba.paw.models;

import java.util.Objects;

public class TimeSlotID {

    protected Integer day;
    protected Integer hour;
    protected Professor professor;

    public TimeSlotID(){}

    public TimeSlotID(Integer day, Integer hour, Professor professor) {
        this.day = day;
        this.hour = hour;
        this.professor = professor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeSlotID that = (TimeSlotID) o;
        return Objects.equals(day, that.day) &&
                Objects.equals(hour, that.hour) &&
                Objects.equals(professor, that.professor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, hour, professor);
    }
}
