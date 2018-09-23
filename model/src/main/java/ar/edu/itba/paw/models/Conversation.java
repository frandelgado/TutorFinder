package ar.edu.itba.paw.models;

import org.joda.time.LocalDateTime;

import java.util.LinkedList;
import java.util.List;

public class Conversation {

    private final Long id;
    private final User user;
    private final Professor professor;
    private final Subject subject;
    private final List<Message> messages;
    private final LocalDateTime latestMessage;


    public Conversation(Long id, final User user, final Professor professor, final Subject subject, LocalDateTime latestMessage) {
        this.id = id;
        this.user = user;
        this.professor = professor;
        this.subject = subject;
        this.latestMessage = latestMessage;
        this.messages = new LinkedList<>();
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Professor getProfessor() {
        return professor;
    }

    public Subject getSubject() {
        return subject;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public LocalDateTime getLatestMessage() {
        return latestMessage;
    }

    public void addMessages(List<Message> messages) {
        this.messages.addAll(messages);
    }

    public boolean belongs(Long id) {return (user.getId().equals(id) || professor.getId().equals(id)); }

    public String getHours() {
        return String.format("%02d", latestMessage.getHourOfDay());
    }

    public String getMinutes() {
        return String.format("%02d", latestMessage.getMinuteOfHour());
    }

    public String getDay() {
        return String.format("%02d", latestMessage.getDayOfMonth());
    }

    public String getMonth() {
        return String.format("%02d", latestMessage.getMonthOfYear());
    }

    public int getYear() {
        return latestMessage.getYear();
    }
}
