package ar.edu.itba.paw.models;

public class Professor extends User{

    private final String description;

    public Professor(Long id, String username, String name, String lastname, String password, String email, String description) {
        super(id, username, name, lastname, password, email);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
