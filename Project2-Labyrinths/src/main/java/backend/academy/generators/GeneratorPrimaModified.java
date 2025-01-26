package backend.academy.generators;

import backend.academy.Maze;
import backend.academy.exceptions.GenerateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Абстрактный класс реализующий общие алгоритмы по генерации лабиринта
 *
 * @version 1.0
 * @autor Георгий Голенков
 * <a href="https://github.com/ITStrongBeast">...</a>
 */
public class GeneratorPrimaModified extends AbstractGenerator {

    @Override public Maze generate(Maze maze) throws GenerateException {
        this.maze = maze;
        int height = maze.height();
        int width = maze.width();

        List<Maze.Coordinate> frontierCells = new ArrayList<>();

        Random random = new Random();
        int startRow = random.nextInt(height);
        int startCol = random.nextInt(width);

        startRow = (startRow % 2 == 0) ? startRow + 1 : startRow;
        startCol = (startCol % 2 == 0) ? startCol + 1 : startCol;

        maze.grid()[startRow][startCol].type(Maze.Cell.Type.PASSAGE);

        addFrontierCells(frontierCells, startRow, startCol);

        while (!frontierCells.isEmpty()) {
            int randomIndex = random.nextInt(frontierCells.size());
            Maze.Coordinate frontierCell = frontierCells.remove(randomIndex);
            List<Maze.Coordinate> neighbors = getInnerNeighbors(frontierCell.row(), frontierCell.col());
            if (!neighbors.isEmpty()) {
                Maze.Coordinate innerNeighbor = neighbors.get(random.nextInt(neighbors.size()));
                removeWall(maze, frontierCell, innerNeighbor);
                maze.grid()[frontierCell.row()][frontierCell.col()].type(Maze.Cell.Type.PASSAGE);
                addFrontierCells(frontierCells, frontierCell.row(), frontierCell.col());
            }
        }

        return maze;
    }

    private void addFrontierCells(List<Maze.Coordinate> frontierCells, int row, int col) {
        addFrontierCell(frontierCells, row - 2, col);
        addFrontierCell(frontierCells, row + 2, col);
        addFrontierCell(frontierCells, row, col - 2);
        addFrontierCell(frontierCells, row, col + 2);
    }

    private void addFrontierCell(List<Maze.Coordinate> frontierCells, int row, int col) {
        if (isInBounds(row, col) && maze.grid()[row][col].type() == Maze.Cell.Type.WALL) {
            frontierCells.add(new Maze.Coordinate(row, col));
            maze.grid()[row][col].type(Maze.Cell.Type.PASSAGE);
        }
    }

    private List<Maze.Coordinate> getInnerNeighbors(int row, int col) {
        List<Maze.Coordinate> neighbors = new ArrayList<>();

        if (isInBounds(row - 2, col) && maze.grid()[row - 2][col].type() == Maze.Cell.Type.PASSAGE) {
            neighbors.add(new Maze.Coordinate(row - 2, col));
        }
        if (isInBounds(row + 2, col) && maze.grid()[row + 2][col].type() == Maze.Cell.Type.PASSAGE) {
            neighbors.add(new Maze.Coordinate(row + 2, col));
        }
        if (isInBounds(row, col - 2) && maze.grid()[row][col - 2].type() == Maze.Cell.Type.PASSAGE) {
            neighbors.add(new Maze.Coordinate(row, col - 2));
        }
        if (isInBounds(row, col + 2) && maze.grid()[row][col + 2].type() == Maze.Cell.Type.PASSAGE) {
            neighbors.add(new Maze.Coordinate(row, col + 2));
        }

        return neighbors;
    }
}
