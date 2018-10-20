package ar.edu.itba.paw.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.util.Base64;

@Entity
@Table(name = "professors")
public class Professor extends User{

    @Column(length = 512, nullable = false)
    private final String description;

    //TODO: No estoy muy seguro del column definition, revisar si guarda apropiadamente una imagen.
    @Lob
    @Column(name = "profile_picture", nullable = false, columnDefinition = "BYTEA")
    private final byte[] picture;

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
