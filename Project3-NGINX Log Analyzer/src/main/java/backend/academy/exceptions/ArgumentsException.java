package backend.academy.exceptions;

public class ArgumentsException extends Exception {
    private final String message;

    public ArgumentsException(String message) {
        this.message = message;
    }

    @Override public String getMessage() {
        return message;
    }
}
