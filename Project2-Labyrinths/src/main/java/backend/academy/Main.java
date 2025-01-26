package backend.academy;

import backend.academy.exceptions.FindException;
import backend.academy.exceptions.GenerateException;
import backend.academy.exceptions.PositionException;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Logger;
import lombok.experimental.UtilityClass;

@UtilityClass public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            new Mediator().run(scanner);
        } catch (IOException e) {
            LOGGER.severe("Console reading error");
            System.err.println("Произошла ошибка ввода");
        } catch (GenerateException e) {
            LOGGER.severe("A maze generation error has occurred");
            LOGGER.severe(e.getMessage());
            System.err.println("Произошла ошибка генерации лабиринта");
        } catch (PositionException e) {
            LOGGER.severe("It is impossible to create a maze due to the proximity of points A and B");
            LOGGER.severe(e.getMessage());
            System.err.println("Невозможно создать лабиринт из-за близкого расположения точек A и B");
        } catch (FindException e) {
            LOGGER.severe("Couldn't find the path from A to B in the maze");
            LOGGER.severe(e.getMessage());
            System.err.println("Не получилось найти путь от A до B в лабиринте");
        }
    }
}
