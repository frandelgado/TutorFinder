package ar.edu.itba.paw.exceptions;

public class EmailAlreadyInUseException extends Exception {
    public EmailAlreadyInUseException() {
    }

    public EmailAlreadyInUseException(String message) {
        super(message);
    }
}
