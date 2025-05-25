package service;

public class AlreadyTakenException extends Exception {
    public AlreadyTakenException() {
        super();
    }
    public AlreadyTakenException(String message) {
        super(message);
    }
}
