package ar.edu.itba.paw.models;

import com.sun.istack.internal.NotNull;
import javax.validation.constraints.Size;

public class Timeslot {

    @NotNull
    @Size(min = 1, max = 7)
    private final Integer day;

    @NotNull
    @Size(max = 24)
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
