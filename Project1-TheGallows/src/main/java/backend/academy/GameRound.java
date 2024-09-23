package backend.academy;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

public class GameRound {
    private final int magicNumbers3;
    private final int magicNumbers6;
    private final int magicNumbers8;
    private final int magicNumbers9;

    private final Map<Integer, Complexity.Pair> complexity;
    private final Map<String, String> dictionary;
    private final boolean useHint;
    private boolean isPlay = true;
    private int corCounter = 0;
    private int incorrectLetterCounter = 0;
    private String word;
    private final char[][] gallows;

    public GameRound(Map<Integer, Complexity.Pair> complexity, Map<String, String> dictionary, boolean useHint) {
        this.complexity = complexity;
        this.dictionary = dictionary;
        this.useHint = useHint;
        ConfigLoader configLoader = new ConfigLoader();
        magicNumbers3 = configLoader.getIntConfigValue("value3");
        magicNumbers6 = configLoader.getIntConfigValue("value6");
        magicNumbers8 = configLoader.getIntConfigValue("value8");
        magicNumbers9 = configLoader.getIntConfigValue("value9");
        gallows = new char[magicNumbers9][magicNumbers6];
    }

    public boolean playRound(BufferedReader reader) throws IOException {
        word = getRandomWord();
        createGallows();
        while (isPlay) {
            motion(reader);
        }
        printGallows();
        System.err.println("Если вы хотите начать новый раунд введите 'new': ");
        String line = reader.readLine();
        return line != null && line.equalsIgnoreCase("new");
    }

    private void motion(BufferedReader reader) throws IOException {
        isPlay = false;
        printStatus();
        if (useHint) {
            System.err.println("Подсказка: " + dictionary.get(word));
        }
        printGallows();
        System.err.println("Отгадайте следующую букву: ");
        String line = reader.readLine();
        if (line != null && line.length() > 1) {
            System.err.println("Вы ввели более одного символа. Попробуйте ещё раз");
        } else if (line == null || line.isEmpty() || Character.toLowerCase(line.charAt(0)) != word.charAt(corCounter)) {
            incorrectLetterCounter++;
            Complexity.Pair pair = complexity.get(incorrectLetterCounter);
            Complexity.Position position = pair.position();
            gallows[position.x()][position.y()] = pair.partGallows();
            System.err.println("Вы допустили ошибку");
        } else {
            corCounter++;
            System.err.println("Вы угадали букву");
        }
        if (corCounter == word.length()) {
            System.err.println("Вы выиграли!");
        } else if (incorrectLetterCounter == complexity.keySet().size()) {
            System.err.println("Вы проиграли!");
        } else {
            isPlay = true;
        }
    }

    private void createGallows() {
        for (int i = 0; i < magicNumbers9; i++) {
            for (int j = 0; j < magicNumbers6; j++) {
                gallows[i][j] = ' ';
            }
        }
        for (int i = 1; i < magicNumbers6; i++) {
            gallows[0][i] = '=';
        }
        for (int i = 1; i < magicNumbers9; i++) {
            gallows[i][1] = '|';
        }
        for (int i = magicNumbers3; i < magicNumbers6; i++) {
            gallows[magicNumbers8][i] = '_';
        }
        gallows[magicNumbers8][0] = '/';
        gallows[magicNumbers8][2] = '\\';
    }

    private String getRandomWord() {
        return dictionary.keySet().toArray()[new Random().nextInt(dictionary.keySet().size())].toString();
    }

    private void printGallows() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < magicNumbers9; i++) {
            for (int j = 0; j < magicNumbers6; j++) {
                sb.append(gallows[i][j]);
            }
            sb.append(System.lineSeparator());
        }
        System.err.println(sb);
    }

    private void printStatus() {
        System.err.print("Слово: ");
        for (int i = 0; i < corCounter; i++) {
            System.err.print(word.charAt(i));
        }
        for (int i = 0; i < word.length() - corCounter; i++) {
            System.err.print("_");
        }
        System.err.println();
    }
}
