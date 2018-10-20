package ar.edu.itba.paw.models;

import javax.persistence.*;


public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subjects_subject_id_seq")
    @SequenceGenerator(sequenceName = "subjects_subject_id_seq", name = "subjects_subject_id_seq",  allocationSize = 1)
    @Column(name = "subject_id")
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Area area;

    public Subject(Long id, String description, String name, Area area) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.area = area;
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
    public Area getArea() {
        return area;
    }
}
