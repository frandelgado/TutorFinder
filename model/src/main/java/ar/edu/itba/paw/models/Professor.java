package ar.edu.itba.paw.models;

public class Professor {

    private final User user;
    private final String description;

    public Professor(User user, String description) {
        this.user = user;
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public String getDescription() {
        return description;
    }
}
