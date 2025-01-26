package backend.academy;

import backend.academy.exceptions.ArgumentsException;
import java.io.IOException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        try {
            new LogReport(args).analysis();
        } catch (ArgumentsException | IOException | InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }
}
