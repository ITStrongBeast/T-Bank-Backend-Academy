package backend.academy;

import java.io.IOException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        try {
            new UserInterface(System.in, System.out).run();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
