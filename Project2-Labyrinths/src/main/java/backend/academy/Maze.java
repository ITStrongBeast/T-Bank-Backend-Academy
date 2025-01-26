package backend.academy;

import lombok.Getter;
import lombok.Setter;

/**
 * Класс Лабиринта.
 *
 * @version 1.0
 * @autor Голенков Георгий
 *     <a href="https://github.com/ITStrongBeast">...</a>
 */

@Getter
public final class Maze {
    /** Поле с координатами точки входа(A) */
    @Setter private Coordinate start;
    /** Поле с координатами точки выхода(A) */
    @Setter private Coordinate end;
    /** Поле с шириной лабиринта */
    @Setter private int height;
    /** Поле с длиной лабиринта */
    @Setter private int width;
    /** Поле с массивом отвечающим за лабиринт */
    private final Cell[][] grid;

    /**
     * Конструктор - создание нового объекта с определенными значениями
     *
     * @param height - ширина
     * @param width  - длина
     * @param start  - координаты точки входа
     * @param end    - координаты точки выхода
     */
    public Maze(int height, int width, Coordinate start, Coordinate end) {
        this.start = start;
        this.end = end;
        this.height = height;
        this.width = width;
        this.grid = new Cell[height][width];
    }

    public Maze(Cell[][] grig) {
        this.grid = grig;
    }

    /**
     * Класс точки из которых состоит лабиринт.
     *
     * @version 1.0
     * @autor Голенков Георгий
     *     <a href="https://github.com/ITStrongBeast">...</a>
     */
    @Getter
    public static class Cell {
        /**
         * Конструктор - создание нового объекта с определенными значениями
         *
         * @param cordinate - положение точки на поле
         * @param type      - тип точки
         */
        public Cell(Coordinate cordinate, Type type) {
            this.cordinate = cordinate;
            this.type = type;
        }

        /**
         * Класс в котором перечислены типы точек.
         *
         * @version 1.0
         * @autor Голенков Георгий
         *     <a href="https://github.com/ITStrongBeast">...</a>
         */
        @Getter
        public enum Type {
            A(0, 'A'),
            B(0, 'B'),
            WAY(0, '■'),
            WALL(-100, '%'),
            PASSAGE(-1, ' '),
            SAND(-10, '░'),
            SWAMP(-20, '≈'),
            HIGHWAY(10, '=');

            /** Поле численным описанием действия поверхности */
            private final int cost;
            /** Поле с символом, которым точка будет помечена при её печати */
            private final char symbol;

            Type(int cost, char symbol) {
                this.cost = cost;
                this.symbol = symbol;
            }
        }

        private final Coordinate cordinate;
        @Setter private Type type;
    }

    /**
     * Класс координат которыми будут обладать точки.
     *
     * @version 1.0
     * @autor Голенков Георгий
     *     <a href="https://github.com/ITStrongBeast">...</a>
     */
    public record Coordinate(int row, int col) {
    }
}
