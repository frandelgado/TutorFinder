package ar.edu.itba.paw.models;

import java.util.LinkedList;
import java.util.List;

public class Conversation {

    private final Long id;
    private final User user;
    private final Professor professor;
    private final Subject subject;
    private final List<Message> messages;


    public Conversation(Long id, final User user, final Professor professor, final Subject subject) {
        this.id = id;
        this.user = user;
        this.professor = professor;
        this.subject = subject;
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

    public void addMessages(List<Message> messages) {
        this.messages.addAll(messages);
    }
}
