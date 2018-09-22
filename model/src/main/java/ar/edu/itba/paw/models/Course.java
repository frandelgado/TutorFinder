package ar.edu.itba.paw.models;

public class Course {

    private final Professor professor;
    private final Subject subject;
    private final String description;
    private final Double price;

    public Course(Professor professor, Subject subject, String description, Double price) {
        this.professor = professor;
        this.subject = subject;
        this.description = description;
        this.price = price;
    }

    public Professor getProfessor() {
        return professor;
    }

    public Subject getSubject() {
        return subject;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }
}
