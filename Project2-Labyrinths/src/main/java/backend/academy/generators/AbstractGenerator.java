package backend.academy.generators;

import backend.academy.Maze;
import backend.academy.exceptions.GenerateException;
import java.util.Random;

/**
 * Абстрактный класс реализующий общие алгоритмы по генерации лабиринта
 *
 * @version 1.0
 * @autor Георгий Голенков
 *     <a href="https://github.com/ITStrongBeast">...</a>
 */
@SuppressWarnings({"checkstyle:MagicNumber"})
public abstract class AbstractGenerator implements Generator {
    /** Поле с лабиринтом */
    protected Maze maze;

    @Override
    public Maze generate(int height, int width, Maze.Coordinate start, Maze.Coordinate end, boolean useSurface)
        throws GenerateException {
        maze = creatingPassages(generate(startGenerate(new Maze(height, width, start, end))));
        maze.grid()[maze.start().row()][maze.start().col()].type(Maze.Cell.Type.A);
        maze.grid()[maze.end().row()][maze.end().col()].type(Maze.Cell.Type.B);
        checkExit(maze, maze.start());
        checkExit(maze, maze.end());
        if (useSurface) {
            return setSurface(maze);
        }
        return maze;
    }

    /**
     * Метод, который задаёт весь лабиринт стенами
     */
    private Maze startGenerate(Maze maze) {
        for (int i = 0; i < maze.height(); i++) {
            for (int j = 0; j < maze.width(); j++) {
                maze.grid()[i][j] = new Maze.Cell(new Maze.Coordinate(i, j), Maze.Cell.Type.WALL);
            }
        }
        maze.grid()[maze.start().row()][maze.start().col()].type(Maze.Cell.Type.PASSAGE);
        maze.grid()[maze.end().row()][maze.end().col()].type(Maze.Cell.Type.PASSAGE);
        return maze;
    }

    /**
     * Метод, который добавляет проходов в уже сгенерированном лабиринте чтобы от A до B было больше 1 пути
     */
    private Maze creatingPassages(Maze maze) {
        Random random = new Random();
        for (int i = 1; i < maze.height() - 1; i += 3) {
            for (int j = 1; j < maze.width() - 1; j += 3) {
                if (maze.grid()[i - 1][j].type() != Maze.Cell.Type.WALL
                    && maze.grid()[i + 1][j].type() != Maze.Cell.Type.WALL
                    || maze.grid()[i][j - 1].type() != Maze.Cell.Type.WALL
                    && maze.grid()[i][j + 1].type() != Maze.Cell.Type.WALL
                    || random.nextInt(maze.height() * maze.width() * 10) < 1) {
                    maze.grid()[i][j].type(Maze.Cell.Type.PASSAGE);
                }
            }
        }
        return maze;
    }

    /**
     * Метод, который при необходимости покрывает проходы различными дополнительными поверхностями
     */
    private Maze setSurface(Maze maze) {
        Random random = new Random();
        for (int i = 1; i < maze.height() - 1; i += 3) {
            for (int j = 1; j < maze.width() - 1; j += 3) {
                if (maze.grid()[i][j].type() != Maze.Cell.Type.PASSAGE) {
                    continue;
                }
                int probability = random.nextInt(100);
                if (probability > 29) {
                    continue;
                }
                if (probability < 10) {
                    maze.grid()[i][j].type(Maze.Cell.Type.SAND);
                } else if (probability < 20) {
                    maze.grid()[i][j].type(Maze.Cell.Type.SWAMP);
                } else {
                    maze.grid()[i][j].type(Maze.Cell.Type.HIGHWAY);
                }
            }
        }
        return maze;
    }

    /**
     * Метод, который проверяет и исправляет лабиринт если нет пути от A до B
     */
    private void checkExit(Maze maze, Maze.Coordinate exit) {
        if (exit.row() == 0) {
            maze.grid()[1][exit.col()].type(Maze.Cell.Type.PASSAGE);
        }
        if (exit.row() == maze.height() - 1) {
            maze.grid()[maze.height() - 2][exit.col()].type(Maze.Cell.Type.PASSAGE);
        }
        if (exit.col() == 0) {
            maze.grid()[exit.row()][1].type(Maze.Cell.Type.PASSAGE);
        }
        if (exit.col() == maze.width() - 1) {
            maze.grid()[exit.row()][maze.width() - 2].type(Maze.Cell.Type.PASSAGE);
        }
    }

    /**
     * Метод удаления стены между ячейками
     */
    protected void removeWall(Maze maze, Maze.Coordinate cell1, Maze.Coordinate cell2) {
        int midRow = (cell1.row() + cell2.row()) / 2;
        int midCol = (cell1.col() + cell2.col()) / 2;
        maze.grid()[midRow][midCol].type(Maze.Cell.Type.PASSAGE);
    }

    /**
     * Метод валидации координат
     */
    protected boolean isInBounds(int row, int col) {
        return row >= 0 && row < maze.height() && col >= 0 && col < maze.width();
    }

    protected abstract Maze generate(Maze maze) throws GenerateException;
}
