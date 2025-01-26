package backend.academy.seekers;

import backend.academy.Maze;
import backend.academy.exceptions.FindException;
import java.util.List;

/**
 * Интерфейс всех классов для поиска пути в лабиринте.
 *
 * @version 1.0
 * @autor Георгий Голенков
 * <a href="https://github.com/ITStrongBeast">...</a>
 */
public interface Solver {
    /**
     * Метод поиска пути в лабиринте от A до B
     * @param maze - лабиринт в котором есть точки A и B
     * @return Список координат на которых будет лежать путь
     */
    List<Maze.Coordinate> solve(Maze maze) throws FindException;
}
