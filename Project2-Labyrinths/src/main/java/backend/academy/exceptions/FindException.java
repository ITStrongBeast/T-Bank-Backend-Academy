package backend.academy.exceptions;

public class FindException extends Exception {
    private final String message;

    public FindException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
