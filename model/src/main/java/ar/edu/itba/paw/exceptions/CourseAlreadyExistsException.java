package ar.edu.itba.paw.exceptions;

public class CourseAlreadyExistsException extends Exception {
    public CourseAlreadyExistsException() {
    }

    public CourseAlreadyExistsException(String message) {
        super(message);
    }
}
