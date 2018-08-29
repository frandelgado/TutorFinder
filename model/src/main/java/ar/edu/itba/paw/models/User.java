package ar.edu.itba.paw.models;

public class User {
    private final Long id;
    private final String name;
    //TODO: la pass deberia estar hasheada no en plaintext
    private final String password;


    public User(Long id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }
}
