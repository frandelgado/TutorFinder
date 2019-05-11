package ar.edu.itba.paw.exceptions;

public class NonexistentConversationException extends Exception {
    public NonexistentConversationException() {
    }

    public NonexistentConversationException(String message) {
        super(message);
    }
}
