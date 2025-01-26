package backend.academy.exceptions;

public class GenerateException extends Exception {
    private final String message;

    public GenerateException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
