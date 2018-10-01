package ar.edu.itba.paw.models;

import java.util.Base64;

public class Area {

    private final Long id;
    private final String description;
    private final String name;
    private final byte[] image;

    public Area(Long id, String description, String name, byte[] image) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.image = image;
    }

    public String getDescription(){
     return description;
    }
    public Long getId(){
        return id;
    }
    public String getName(){
        return name;
    }

    public String getImage() {
        return new String(Base64.getEncoder().encode(image));
    }
}
