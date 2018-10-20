package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.Base64;
import java.util.List;

@Entity
@Table(name = "professors")
public class Professor extends User{

    @Column(length = 512, nullable = false)
    private String description;

    //TODO: No estoy muy seguro del column definition, revisar si guarda apropiadamente una imagen.
    @Lob
    @Column(name = "profile_picture", nullable = false, columnDefinition = "BYTEA")
    private byte[] picture;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = false, mappedBy = "professor")
    private List<Course> courses;

    public Professor(){

    }

    public Professor(String username, String name, String lastname, String password, String email, String description, byte[] picture) {
        super(username, name, lastname, password, email);
        this.description = description;
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public String getPicture() {
        return new String(Base64.getEncoder().encode(picture));
    }
}
