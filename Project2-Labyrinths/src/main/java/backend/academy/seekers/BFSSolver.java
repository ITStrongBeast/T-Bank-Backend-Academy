package backend.academy.seekers;

import backend.academy.Maze;
import backend.academy.exceptions.FindException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Класс для поиска пути в лабиринте на основе алгоритма поиска в ширину (BFS).
 *
 * @version 1.0
 * @autor Георгий Голенков
 * <a href="https://github.com/ITStrongBeast">...</a>
 */
public class BFSSolver implements Solver {

    @Override
    public List<Maze.Coordinate> solve(Maze maze) throws FindException {
        Maze.Coordinate start = maze.start();
        Maze.Coordinate end = maze.end();

        boolean[][] visited = new boolean[maze.height()][maze.width()];

        Map<Maze.Coordinate, Maze.Coordinate> cameFrom = new HashMap<>();

        Deque<Maze.Coordinate> queue = new LinkedList<>();
        queue.add(start);
        visited[start.row()][start.col()] = true;
        cameFrom.put(start, null);

        Maze.Coordinate[] directions = {
            new Maze.Coordinate(-1, 0),
            new Maze.Coordinate(1, 0),
            new Maze.Coordinate(0, -1),
            new Maze.Coordinate(0, 1)
        };

        while (!queue.isEmpty()) {
            Maze.Coordinate current = queue.poll();
            if (current.equals(end)) {
                return reconstructPath(cameFrom, end);
            }
            for (Maze.Coordinate direction : directions) {
                int newRow = current.row() + direction.row();
                int newCol = current.col() + direction.col();
                Maze.Coordinate neighbor = new Maze.Coordinate(newRow, newCol);
                if (isInBounds(maze, newRow, newCol)
                    && !visited[newRow][newCol]
                    && maze.grid()[newRow][newCol].type() != Maze.Cell.Type.WALL) {
                    queue.add(neighbor);
                    visited[newRow][newCol] = true;
                    cameFrom.put(neighbor, current);
                }
            }
        }
        throw new FindException("");
    }

    /**
     * Метод получения окончательного пути.
     * @param cameFrom - Таблица с переходами по точкам
     * @return path - Список координат на которых будет лежать путь
     */
    private List<Maze.Coordinate> reconstructPath(Map<Maze.Coordinate, Maze.Coordinate> cameFrom, Maze.Coordinate end) {
        List<Maze.Coordinate> path = new ArrayList<>();
        for (Maze.Coordinate at = end; at != null; at = cameFrom.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);
        return path;
    }

    /**
     * Метод валидации координат
     */
    private boolean isInBounds(Maze maze, int row, int col) {
        return row >= 0 && row < maze.height() && col >= 0 && col < maze.width();
    }
}
