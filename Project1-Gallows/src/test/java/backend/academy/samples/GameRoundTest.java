package backend.academy.samples;

import backend.academy.Complexity;
import backend.academy.GameRound;
import backend.academy.ListWords;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class GameRoundTest {

    private GameRound gameRound;

    @BeforeEach
    public void setUp() {
        Map<String, String> dictionary = ListWords.ANIMALS.getHint();
        Map<Integer, Complexity.Pair> complexity = Complexity.EASY.getValue();
        gameRound = new GameRound(complexity, dictionary, true);
    }

    @Test
    void testMotion() throws Exception {
        Map<String, String> dictionary = new HashMap<>();
        dictionary.put("apple", "A type of fruit");

        Map<Integer, Complexity.Pair> complexity = Complexity.EASY.getValue();

        GameRound gameRound = new GameRound(complexity, dictionary, true);

        setPrivateField(gameRound, "word", "apple");
        setPrivateField(gameRound, "corCounter", 0);
        setPrivateField(gameRound, "incorrectLetterCounter", 0);

        String simulatedUserInput = "a\nx\np\n";
        BufferedReader reader = new BufferedReader(new StringReader(simulatedUserInput));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Method motionMethod = GameRound.class.getDeclaredMethod("motion", BufferedReader.class);
        motionMethod.setAccessible(true);

        motionMethod.invoke(gameRound, reader);

        int correctLetterCounter = getPrivateField(gameRound, "corCounter");
        int incorrectLetterCounter = getPrivateField(gameRound, "incorrectLetterCounter");
        assertEquals(1, correctLetterCounter);
        assertEquals(0, incorrectLetterCounter);
        assertTrue((Boolean) getPrivateField(gameRound, "isPlay"));

        motionMethod.invoke(gameRound, reader);

        incorrectLetterCounter = getPrivateField(gameRound, "incorrectLetterCounter");
        assertEquals(1, incorrectLetterCounter);
        assertTrue((Boolean) getPrivateField(gameRound, "isPlay"));

        System.setOut(System.out);
    }

    private void setPrivateField(Object object, String fieldName, Object value) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }

    private <T> T getPrivateField(Object object, String fieldName) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(object);
    }

    @Test
    void testCreateGallows()
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Method createGallowsMethod = GameRound.class.getDeclaredMethod("createGallows");
        createGallowsMethod.setAccessible(true);
        createGallowsMethod.invoke(gameRound);

        Field gallowsField = GameRound.class.getDeclaredField("gallows");
        gallowsField.setAccessible(true);
        char[][] gallows = (char[][]) gallowsField.get(gameRound);

        for (int i = 1; i < 6; i++) {
            assertEquals('=', gallows[0][i]);
        }

        for (int i = 1; i < 9; i++) {
            assertEquals('|', gallows[i][1]);
        }

        assertEquals('/', gallows[8][0]);
        assertEquals('\\', gallows[8][2]);
        for (int i = 3; i < 6; i++) {
            assertEquals('_', gallows[8][i]);
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 6; j++) {
                if (!(i == 0 && j >= 1) && !(i > 0 && j == 1) && i != 8) {
                    assertEquals(' ', gallows[i][j]);
                }
            }
        }
    }

    @Test
    void testGetRandomWord() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Set<String> validValues = ListWords.ANIMALS.getHint().keySet();
        Set<String> results = new HashSet<>();

        Method getRandomWordMethod = GameRound.class.getDeclaredMethod("getRandomWord");
        getRandomWordMethod.setAccessible(true);
        String randomWord = (String) getRandomWordMethod.invoke(gameRound);

        for (int i = 0; i < 200; i++) {
            results.add((String) getRandomWordMethod.invoke(gameRound));
        }
        Assertions.assertEquals(ListWords.ANIMALS.getHint().size(), results.size());

        Assertions.assertTrue(validValues.contains(randomWord));
    }

    @Test
    void testPrintGallows() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method createGallowsMethod = GameRound.class.getDeclaredMethod("createGallows");
        createGallowsMethod.setAccessible(true);
        createGallowsMethod.invoke(gameRound);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outContent));

        Method printGallowsMethod = GameRound.class.getDeclaredMethod("printGallows");
        printGallowsMethod.setAccessible(true);
        printGallowsMethod.invoke(gameRound);

        String expectedOutput = " =====" + System.lineSeparator() +
            " |    " + System.lineSeparator() +
            " |    " + System.lineSeparator() +
            " |    " + System.lineSeparator() +
            " |    " + System.lineSeparator() +
            " |    " + System.lineSeparator() +
            " |    " + System.lineSeparator() +
            " |    " + System.lineSeparator() +
            "/|\\___" + System.lineSeparator() +
            System.lineSeparator();

        assertEquals(expectedOutput, outContent.toString());

        System.setOut(System.out);
    }

    @Test
    void testPrintStatus()
        throws IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException {
        Field wordField = GameRound.class.getDeclaredField("word");
        wordField.setAccessible(true);
        wordField.set(gameRound, "apple");

        Field correctLetterCounterField = GameRound.class.getDeclaredField("corCounter");
        correctLetterCounterField.setAccessible(true);
        correctLetterCounterField.setInt(gameRound, 2);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outContent));

        Method printStatusMethod = GameRound.class.getDeclaredMethod("printStatus");
        printStatusMethod.setAccessible(true);
        printStatusMethod.invoke(gameRound);

        String expectedOutput = "Слово: ap___" + System.lineSeparator();

        correctLetterCounterField.setInt(gameRound, 2);

        assertEquals(expectedOutput, outContent.toString());

        System.setOut(System.out);
    }
}
