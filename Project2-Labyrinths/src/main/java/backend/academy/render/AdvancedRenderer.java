package backend.academy.render;

import java.util.HashMap;
import java.util.Map;
import static backend.academy.Maze.Cell.Type.WALL;

/**
 * Класс представления лабиринта в виде строки с использованием продвинутых UNICODE символов
 *
 * @version 1.0
 * @autor Георгий Голенков
 *     <a href="https://github.com/ITStrongBeast">...</a>
 */
public class AdvancedRenderer extends AbstractRenderer {
    /** Поле с перечислением всех символов */
    Map<String, Character> symbols = new HashMap<>(Map.ofEntries(
        Map.entry("0000", '*'),
        Map.entry("1000", '│'),
        Map.entry("0001", '│'),
        Map.entry("1001", '│'),
        Map.entry("0100", '─'),
        Map.entry("0010", '─'),
        Map.entry("0110", '─'),
        Map.entry("1010", '└'),
        Map.entry("0011", '┌'),
        Map.entry("0101", '┐'),
        Map.entry("1100", '┘'),
        Map.entry("1011", '├'),
        Map.entry("1101", '┤'),
        Map.entry("0111", '┬'),
        Map.entry("1110", '┴'),
        Map.entry("1111", '┼')
    ));

    /**
     * Метод, который превращает каждую точку лабиринта в символ в зависимости от её типа
     */
    protected String renderAll() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < maze.height(); i++) {
            for (int j = 0; j < maze.width(); j++) {
                if (grid[i][j].type() == WALL) {
                    result.append(getWall(i, j));
                } else {
                    result.append(grid[i][j].type().symbol());
                }
            }
            result.append(System.lineSeparator());
        }
        return result.toString();
    }

    /**
     * Метод особенной обработки стен
     */
    private char getWall(int x, int y) {
        StringBuilder result = new StringBuilder();
        result.append(isValid(x - 1, y) && grid[x - 1][y].type() == WALL ? '1' : '0');
        result.append(isValid(x, y - 1) && grid[x][y - 1].type() == WALL ? '1' : '0');
        result.append(isValid(x, y + 1) && grid[x][y + 1].type() == WALL ? '1' : '0');
        result.append(isValid(x + 1, y) && grid[x + 1][y].type() == WALL ? '1' : '0');
        return symbols.getOrDefault(result.toString(), ' ');
    }

    private boolean isValid(int rx, int ry) {
        return !(rx < 0 || rx > maze.height() - 1 || ry < 0 || ry > maze.width() - 1);
    }
}
