package sk.foxer.flashcard.api.exception;

public class DangerousInputException extends RuntimeException {
    public DangerousInputException(String message) {
        super(message);
    }
}
