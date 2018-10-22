package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.Base64;

@Entity
@Table(name = "areas")
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "areas_area_id_seq")
    @SequenceGenerator(sequenceName = "areas_area_id_seq", name = "areas_area_id_seq",  allocationSize = 1)
    @Column(name = "area_id")
    private Long id;

    @Column(nullable = false, length = 512)
    private String description;

    @Column(nullable = false, length = 128, unique = true)
    private String name;

    @Column(nullable = false)
    private byte[] image;

    Area(){}

    public Area(String description, String name, byte[] image) {
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
