package ar.edu.itba.paw.exceptions;

public class TimeslotAllocatedException extends Exception {
    public TimeslotAllocatedException() {
    }

    public TimeslotAllocatedException(String message) {
        super(message);
    }
}
