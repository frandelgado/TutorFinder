package ar.edu.itba.paw.models;

public class User {
    private final Long id;
    private final String username;
    private final String name;
    private final String lastname;
    private final String password;
    private final String email;


    public User(Long id, String username, String name, String lastname, String password, String email) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.lastname = lastname;
        this.password = password;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
