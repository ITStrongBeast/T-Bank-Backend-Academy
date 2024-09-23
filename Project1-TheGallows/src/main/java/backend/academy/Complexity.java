package backend.academy;

import java.util.Map;
import lombok.Getter;

@Getter
public enum Complexity {
    EASY(9, Map.of(
        1, new Pair(new Position(1, 4), '|'),
        2, new Pair(new Position(2, 4), '|'),
        3, new Pair(new Position(3, 4), '0'),
        4, new Pair(new Position(4, 4), '|'),
        5, new Pair(new Position(4, 3), '/'),
        6, new Pair(new Position(4, 5), '\\'),
        7, new Pair(new Position(5, 4), '|'),
        8, new Pair(new Position(6, 3), '/'),
        9, new Pair(new Position(6, 5), '\\')
    )),
    MEDIUM(7, Map.of(
        1, new Pair(new Position(1, 4), '|'),
        2, new Pair(new Position(2, 4), '0'),
        3, new Pair(new Position(3, 4), '|'),
        4, new Pair(new Position(3, 3), '/'),
        5, new Pair(new Position(3, 5), '\\'),
        6, new Pair(new Position(4, 3), '/'),
        7, new Pair(new Position(4, 5), '\\')
    )),
    HARD(5, Map.of(
        1, new Pair(new Position(1, 4), '|'),
        2, new Pair(new Position(2, 4), '0'),
        3, new Pair(new Position(3, 4), '|'),
        4, new Pair(new Position(4, 3), '/'),
        5, new Pair(new Position(4, 5), '\\')
    ));

    private final Map<Integer, Pair> value;
    private final int complexity;

    Complexity(int complexity, Map<Integer, Complexity.Pair> integerPairMap) {
        this.complexity = complexity;
        this.value = integerPairMap;
    }

    public int getComplexity() {
        return complexity;
    }

    public Map<Integer, Pair> getValue() {
        return value;
    }

    public record Position(int x, int y) {}

    public record Pair(Position position, char partGallows) {}
}
