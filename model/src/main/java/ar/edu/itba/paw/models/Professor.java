package ar.edu.itba.paw.models;

import java.util.Base64;

public class Professor extends User{

    private final String description;

    private final byte[] picture;

    public Professor(Long id, String username, String name, String lastname, String password, String email, String description, byte[] picture) {
        super(id, username, name, lastname, password, email);
        this.description = description;
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public String getPicture() {
        if(picture == null)
            return "";
        return new String(Base64.getEncoder().encode(picture));
    }
}
