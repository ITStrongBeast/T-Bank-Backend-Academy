package backend.academy.generators;

import backend.academy.Maze;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * Абстрактный класс реализующий общие алгоритмы по генерации лабиринта
 *
 * @version 1.0
 * @autor Георгий Голенков
 *     <a href="https://github.com/ITStrongBeast">...</a>
 */
@SuppressWarnings({"checkstyle:MagicNumber"})
public class GeneratorPrima extends AbstractGenerator {

    public Maze generate(Maze maze) {
        this.maze = maze;
        int height = maze.height();
        int width = maze.width();

        PriorityQueue<Edge> edgeQueue = new PriorityQueue<>();

        boolean[][] visited = new boolean[height][width];

        Random random = new Random();
        int startRow = random.nextInt(height);
        int startCol = random.nextInt(width);

        startRow = (startRow % 2 == 0) ? startRow + 1 : startRow;
        startCol = (startCol % 2 == 0) ? startCol + 1 : startCol;

        Maze.Coordinate start = new Maze.Coordinate(startRow, startCol);
        maze.grid()[startRow][startCol].type(Maze.Cell.Type.PASSAGE);
        visited[startRow][startCol] = true;

        addEdges(edgeQueue, start, visited);

        while (!edgeQueue.isEmpty()) {
            Edge edge = edgeQueue.poll();
            if (visited[edge.to.row()][edge.to.col()]) {
                continue;
            }
            removeWall(maze, edge.from, edge.to);
            maze.grid()[edge.to.row()][edge.to.col()].type(Maze.Cell.Type.PASSAGE);
            visited[edge.to.row()][edge.to.col()] = true;
            addEdges(edgeQueue, edge.to, visited);
        }
        return maze;
    }

    /**
     * Метод добавления рёбер в очередь с приоритетом
     */
    private void addEdges(PriorityQueue<Edge> edgeQueue, Maze.Coordinate cell, boolean[][] visited) {
        int row = cell.row();
        int col = cell.col();
        Random random = new Random();

        addEdge(edgeQueue, row - 2, col, cell, random, visited);
        addEdge(edgeQueue, row + 2, col, cell, random, visited);
        addEdge(edgeQueue, row, col - 2, cell, random, visited);
        addEdge(edgeQueue, row, col + 2, cell, random, visited);
    }

    /**
     * Метод добавления ребра к соседу, если он находится в пределах лабиринта и не был посещён
     */
    private void addEdge(
        PriorityQueue<Edge> edgeQueue,
        int newRow,
        int newCol,
        Maze.Coordinate from,
        Random random,
        boolean[][] visited
    ) {
        if (isInBounds(newRow, newCol) && !visited[newRow][newCol]) {
            int weight = random.nextInt(100);
            Maze.Coordinate to = new Maze.Coordinate(newRow, newCol);
            edgeQueue.add(new Edge(from, to, weight));
        }
    }

    public static class Edge implements Comparable<Edge> {
        Maze.Coordinate from;
        Maze.Coordinate to;
        int weight;

        Edge(Maze.Coordinate from, Maze.Coordinate to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        @Override
        public int compareTo(Edge other) {
            return Integer.compare(this.weight, other.weight);
        }
    }
}
