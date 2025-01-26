package backend.academy.render;

import backend.academy.Maze;
import java.util.List;

/**
 * Интерфейс классов для представления лабиринтов в виде строки
 *
 * @version 1.0
 * @autor Георгий Голенков
 *     <a href="https://github.com/ITStrongBeast">...</a>
 */
public interface Renderer {
    /**
     * Метод представления всех клеток лабиринта
     *
     * @param maze - лабиринт который необходимо отрендерить
     */
    String render(Maze maze);

    /**
     * Метод поиска пути в лабиринте от A до B
     *
     * @param maze - лабиринт который необходимо отрендерить
     * @param path - лист пути который нужно отрендерить поверх лабиринта
     */
    String render(Maze maze, List<Maze.Coordinate> path);
}
