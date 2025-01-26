package backend.academy.samples;

import backend.academy.Complexity;
import backend.academy.Game;
import backend.academy.ListWords;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

class GameTest {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.err;

    private ByteArrayOutputStream testOut;
    private final Game game = new Game();

    @BeforeEach
    public void setUp() {
        testOut = new ByteArrayOutputStream();
        System.setErr(new PrintStream(testOut));
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalIn);
        System.setOut(originalOut);

        try {
            resetNewRound();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetNewRound() throws Exception {
        Field newRoundField = Game.class.getDeclaredField("newRound");
        newRoundField.setAccessible(true);
        newRoundField.set(null, true);
    }

    private void provideInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    @Test
    void testStartGameWithValidInput() {
        String input = """
            HARD
            AnImals
            yes
            no
            """;
        provideInput(input);

        game.startGame();

        String output = testOut.toString();
        Assertions.assertTrue(output.contains("Сложность: HARD"));
        Assertions.assertTrue(output.contains("Категория слов: ANIMALS"));
        Assertions.assertTrue(output.contains("Вы можете допустить 5 ошибок"));
    }

    @Test
    void testStartGameWithInvalidComplexity() {
        String input = """
            INVALID
            cars
            yes
            """;
        provideInput(input);

        game.startGame();

        String output = testOut.toString();
        Assertions.assertTrue(output.contains("Сложность:"));
        Assertions.assertTrue(output.contains("Категория слов: CARS"));
    }

    @Test
    void testStartGameWithInvalidCategory() {
        String input = """
            HARD
            INVALID
            yes
            """;
        provideInput(input);

        game.startGame();

        String output = testOut.toString();
        Assertions.assertTrue(output.contains("Сложность: HARD"));
        Assertions.assertTrue(output.contains("Категория слов:"));
    }

    @Test
    void testStartGameWithHint() {
        String input = """
            MEDIUM
            Templates
            yes
            """;
        provideInput(input);

        game.startGame();

        String output = testOut.toString();
        Assertions.assertTrue(output.contains("Сложность: MEDIUM"));
        Assertions.assertTrue(output.contains("Категория слов: TEMPLATES"));
        Assertions.assertTrue(output.contains("Вы можете допустить 7 ошибок"));
    }

    @Test
    void testStartGameWithoutHint() {
        String input = """
            EASY
            Cars
            no
            """;
        provideInput(input);

        game.startGame();

        String output = testOut.toString();
        Assertions.assertTrue(output.contains("Сложность: EASY"));
        Assertions.assertTrue(output.contains("Категория слов: CARS"));
        Assertions.assertTrue(output.contains("Вы можете допустить 9 ошибок"));
    }

    @Test
    void testRandomDictionary()
        throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Set<ListWords> validValues = EnumSet.allOf(ListWords.class);
        Set<ListWords> results = new HashSet<>();

        Method getRandomDictionaryMethod = Game.class.getDeclaredMethod("getRandomEnum", Class.class);
        getRandomDictionaryMethod.setAccessible(true);
        ListWords randomWord = (ListWords) getRandomDictionaryMethod.invoke(game, ListWords.class);

        for (int i = 0; i < 100; i++) {
            results.add((ListWords) getRandomDictionaryMethod.invoke(game, ListWords.class));
        }
        Assertions.assertEquals(4, results.size()); // проверка рандомности

        Assertions.assertTrue(validValues.contains(randomWord)); // проверка принадлежности к коллекции
    }

    @Test
    void testRandomComplexity()
        throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Set<Complexity> validValues = EnumSet.allOf(Complexity.class);
        Set<Complexity> results = new HashSet<>();

        Method getRandomComplexityMethod = Game.class.getDeclaredMethod("getRandomEnum", Class.class);
        getRandomComplexityMethod.setAccessible(true);
        Complexity randomWord = (Complexity) getRandomComplexityMethod.invoke(game, Complexity.class);

        for (int i = 0; i < 100; i++) {
            results.add((Complexity) getRandomComplexityMethod.invoke(game, Complexity.class));
        }
        Assertions.assertEquals(3, results.size()); // проверка рандомности

        Assertions.assertTrue(validValues.contains(randomWord)); // проверка принадлежности к коллекции
    }
}
