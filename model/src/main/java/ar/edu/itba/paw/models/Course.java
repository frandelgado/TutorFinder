package ar.edu.itba.paw.models;

public class Course {

    private final Professor professor;
    private final Subject subject;
    private final String description;
    private final Double price;
    private final Double rating;

    public Course(Professor professor, Subject subject, String description, Double price, Double rating) {
        this.professor = professor;
        this.subject = subject;
        this.description = description;
        this.price = price;
        this.rating = rating;
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

    public Double getRating() {
        return rating;
    }
}
