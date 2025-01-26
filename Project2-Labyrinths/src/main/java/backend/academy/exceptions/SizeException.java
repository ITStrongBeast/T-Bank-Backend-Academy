package backend.academy.exceptions;

public class SizeException extends RuntimeException {
    private final String message;

    public SizeException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
