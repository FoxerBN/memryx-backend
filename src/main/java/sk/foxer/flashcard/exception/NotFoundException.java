package sk.foxer.flashcard.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String resource, Object id) {
        super(resource + " not found with id: " + id);
    }
}
