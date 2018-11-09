package ar.edu.itba.paw.models;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_user_id_seq")
    @SequenceGenerator(sequenceName = "users_user_id_seq", name = "users_user_id_seq",  allocationSize = 1)
    @Column(name = "user_id")
    private  Long id;

    @Column(length = 128, nullable = false, unique = true)
    private String username;

    @Column(length = 128, nullable = false)
    private String name;

    @Column(length = 128, nullable = false)
    private String lastname;

    @Column(length = 64, nullable = false)
    private String password;

    @Column(length = 512, unique = true, nullable = false)
    private String email;

    User(){}

    public User(String username, String name, String lastname, String password, String email) {
        this.username   = username;
        this.name       = name;
        this.lastname   = lastname;
        this.password   = password;
        this.email      = email;
    }

    public User(Long id, String username, String name, String lastname, String password, String email) {
        this(username, name, lastname, password, email);
        this.id         = id;
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

    public void setPassword(String password) {
        this.password = password;
    }
}
