package backend.academy.samples;

import backend.academy.Mediator;
import backend.academy.exceptions.GenerateException;
import backend.academy.exceptions.PositionException;
import backend.academy.exceptions.FindException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MediatorTest {

    private Mediator mediator;

    @BeforeEach
    void setUp() {
        mediator = new Mediator();
    }

    @Test
    void testRunWithValidInput() throws IOException, PositionException, GenerateException, FindException {
        String userInput = """
                5 5
                l 1
                r 3
                Kraskala
                no
                base
                find
                bfs
                """;
        ByteArrayInputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        mediator.run(new Scanner(System.in));

        String consoleOutput = outContent.toString();
        assertTrue(consoleOutput.contains("Введите сторону"));
        assertTrue(consoleOutput.contains("Введите сторону(l-левая r-правая u-верхняя d-нижняя) и позицию"));
        assertTrue(consoleOutput.contains("Найти решение (write: find)"));
    }

    @Test
    void testRunWithPositionException() {
        String userInput = """
                5 5
                l 1
                l 2
                Kraskala
                no
                base
                """;
        ByteArrayInputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        assertThrows(PositionException.class, () -> mediator.run(new Scanner(System.in)));
    }

    @Test
    void testRunWithFindOption() throws IOException, PositionException, GenerateException, FindException {
        String userInput = """
                5 5
                l 1
                r 3
                Kraskala
                no
                adv
                find
                astar
                """;
        ByteArrayInputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        mediator.run(new Scanner(System.in));
        String consoleOutput = outContent.toString();
        assertTrue(consoleOutput.contains("Найти решение (write: find)"));
        assertTrue(consoleOutput.contains("Выберите алгоритм поиска пути от A до B в лабиринте"));
    }
}
