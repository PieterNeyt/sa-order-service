package be.kdg.sa.backend.domain;

public class ActionNotPossibleException extends RuntimeException {
    public ActionNotPossibleException(String message) {
        super(message);
    }
}
