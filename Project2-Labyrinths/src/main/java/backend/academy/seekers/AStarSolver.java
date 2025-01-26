package backend.academy.seekers;

import backend.academy.Maze;
import backend.academy.exceptions.FindException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Класс для поиска пути в лабиринте на основе алгоритма A-star (A*).
 *
 * @version 1.0
 * @autor Георгий Голенков
 *     <a href="https://github.com/ITStrongBeast">...</a>
 */
public class AStarSolver implements Solver {
    @Override
    public List<Maze.Coordinate> solve(Maze maze) throws FindException {
        Maze.Coordinate start = maze.start();
        Maze.Coordinate end = maze.end();

        boolean[][] visited = new boolean[maze.height()][maze.width()];

        Map<Maze.Coordinate, Maze.Coordinate> cameFrom = new HashMap<>();

        Map<Maze.Coordinate, Integer> gScore = new HashMap<>();
        Map<Maze.Coordinate, Integer> fScore = new HashMap<>();

        PriorityQueue<Maze.Coordinate> openSet = new PriorityQueue<>(
            Comparator.comparingInt(fScore::get));

        gScore.put(start, 0);
        fScore.put(start, heuristic(start, end));
        openSet.add(start);

        Maze.Coordinate[] directions = {
            new Maze.Coordinate(-1, 0),
            new Maze.Coordinate(1, 0),
            new Maze.Coordinate(0, -1),
            new Maze.Coordinate(0, 1)
        };

        while (!openSet.isEmpty()) {
            Maze.Coordinate current = openSet.poll();
            if (current.equals(end)) {
                return reconstructPath(cameFrom, end);
            }
            visited[current.row()][current.col()] = true;

            for (Maze.Coordinate direction : directions) {
                int newRow = current.row() + direction.row();
                int newCol = current.col() + direction.col();
                Maze.Coordinate neighbor = new Maze.Coordinate(newRow, newCol);
                if (isInBounds(maze, newRow, newCol)
                    && !visited[newRow][newCol]
                    && maze.grid()[newRow][newCol].type() != Maze.Cell.Type.WALL) {
                    int tentativeGScore = gScore.get(current) + 1;
                    if (tentativeGScore < gScore.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                        cameFrom.put(neighbor, current);
                        gScore.put(neighbor, tentativeGScore);
                        fScore.put(neighbor, tentativeGScore + heuristic(neighbor, end));
                        if (!openSet.contains(neighbor)) {
                            openSet.add(neighbor);
                        }
                    }
                }
            }
        }
        throw new FindException("");
    }

    /**
     * Метод получения окончательного пути.
     *
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

    private int heuristic(Maze.Coordinate a, Maze.Coordinate b) {
        return Math.abs(a.row() - b.row()) + Math.abs(a.col() - b.col());
    }

    /**
     * Метод валидации координат
     */
    private boolean isInBounds(Maze maze, int row, int col) {
        return row >= 0 && row < maze.height() && col >= 0 && col < maze.width();
    }
}
