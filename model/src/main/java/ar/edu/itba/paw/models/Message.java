package ar.edu.itba.paw.models;

import org.joda.time.LocalDateTime;

public class Message {

    private final Long id;
    private final User sender;
    private final String text;
    private final LocalDateTime created;

    public Message(Long id, User sender, String text, LocalDateTime created) {
        this.id = id;
        this.sender = sender;
        this.text = text;
        this.created = created;
    }

    public Long getId() {
        return id;
    }

    public User getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public String getHours() {
        return String.format("%02d", created.getHourOfDay());
    }

    public String getMinutes() {
        return String.format("%02d", created.getMinuteOfHour());
    }

    public String getDay() {
        return String.format("%02d", created.getDayOfMonth());
    }

    public String getMonth() {
        return String.format("%02d", created.getMonthOfYear());
    }

    public int getYear() {
        return created.getYear();
    }
}
