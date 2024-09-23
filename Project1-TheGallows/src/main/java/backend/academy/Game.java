package backend.academy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Game {
    private boolean newRound = true;

    public Game() {
    }

    public void startGame() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
            System.err.println(
                """
                    Введите уровень сложности игры:
                    * HARD (5 ошибок)
                    * MEDIUM (7 ошибок)
                    * EASY (9 ошибок)"""
            );
            String line = reader.readLine();
            Complexity complexity = (line == null || line.isEmpty() || checkEnum(Complexity.class, line))
                ? getRandomEnum(Complexity.class) : Complexity.valueOf(line.toUpperCase());
            System.err.println(
                """
                    Введите категорию слов:
                     * Templates
                     * Paradigms
                     * Cars
                     * Animals"""
            );
            line = reader.readLine();
            ListWords dictionary = (line == null || line.isEmpty() || checkEnum(ListWords.class, line))
                ? getRandomEnum(ListWords.class) : ListWords.valueOf(line.toUpperCase());
            System.err.println("Если вы хотите использовать подсказку введите 'yes': ");
            line = reader.readLine();
            boolean useHint = line != null && line.equalsIgnoreCase("yes");
            while (newRound) {
                System.err.printf("""
                    Начинаем игру
                    Сложность: %s
                    Категория слов: %s
                    Вы можете допустить %d ошибок
                    """, complexity.name(), dictionary.name(), complexity.getComplexity());
                GameRound round = new GameRound(complexity.getValue(), dictionary.getHint(), useHint);
                newRound = round.playRound(reader);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private <T extends Enum<T>> boolean checkEnum(Class<T> enumClass, String line) {
        for (T word : enumClass.getEnumConstants()) {
            if (word.name().equalsIgnoreCase(line)) {
                return false;
            }
        }
        return true;
    }

    private <T extends Enum<T>> T getRandomEnum(Class<T> enumClass) {
        return enumClass.getEnumConstants()[(int) (Math.random() * enumClass.getEnumConstants().length)];
    }
}
