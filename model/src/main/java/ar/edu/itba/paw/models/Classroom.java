package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.List;

@Entity
public class Classroom {

    @Id
    @OneToOne(fetch = FetchType.EAGER, optional = false)
    private Course course;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Professor professor;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<User> students;
}
