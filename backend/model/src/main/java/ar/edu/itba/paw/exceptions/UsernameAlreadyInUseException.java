package ar.edu.itba.paw.exceptions;

public class UsernameAlreadyInUseException extends Exception {
    public UsernameAlreadyInUseException() {
    }

    public UsernameAlreadyInUseException(String message) {
        super(message);
    }
}
