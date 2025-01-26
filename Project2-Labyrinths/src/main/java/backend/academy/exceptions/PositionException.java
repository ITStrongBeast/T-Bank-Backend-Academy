package backend.academy.exceptions;

public class PositionException extends Exception {
    private final String message;

    public PositionException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
