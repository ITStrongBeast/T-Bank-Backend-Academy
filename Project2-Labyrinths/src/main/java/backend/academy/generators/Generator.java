package backend.academy.generators;

import backend.academy.Maze;
import backend.academy.exceptions.GenerateException;

/**
 * Интерфейс классов по генерации лабиринтов
 *
 * @version 1.0
 * @autor Георгий Голенков
 * <a href="https://github.com/ITStrongBeast">...</a>
 */
public interface Generator {
    /**
     * Метод представления всех клеток лабиринта
     * @param height - ширина лабиринта
     * @param width - длина лабиринта
     * @param start - координаты точки входа в лабиринт
     * @param end - координаты точки выхода из лабиринта
     * @param useSurface - значение, при наличии которого в лабиринт добавляются дополнительные поверхности
     */
    Maze generate(int height, int width, Maze.Coordinate start, Maze.Coordinate end, boolean useSurface)
        throws GenerateException;
}
