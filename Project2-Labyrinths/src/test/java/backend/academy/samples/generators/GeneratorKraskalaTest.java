package backend.academy.samples.generators;

import backend.academy.Maze;
import backend.academy.Maze.Cell;
import backend.academy.exceptions.GenerateException;
import backend.academy.generators.GeneratorKraskala;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static backend.academy.Maze.Cell.Type.*;
import static org.junit.jupiter.api.Assertions.*;

public class GeneratorKraskalaTest {

    private GeneratorKraskala generatorKraskala;
    private Maze maze;

    @BeforeEach
    public void setUp() {
        generatorKraskala = new GeneratorKraskala();

        Cell[][] grid = new Cell[5][5];
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                grid[row][col] = new Cell(new Maze.Coordinate(row, col), WALL); // Все клетки изначально - это стены
            }
        }

        maze = new Maze(grid);
        maze.width(5);
        maze.height(5);
        maze.start(new Maze.Coordinate(0, 1));
        maze.end(new Maze.Coordinate(4, 1));
    }

    @Test
    public void testGenerateMaze() throws GenerateException {
        Maze generatedMaze = generatorKraskala.generate(maze);

        boolean hasPassages = false;
        for (int row = 0; row < generatedMaze.height(); row++) {
            for (int col = 0; col < generatedMaze.width(); col++) {
                if (generatedMaze.grid()[row][col].type() == PASSAGE) {
                    hasPassages = true;
                    break;
                }
            }
        }

        assertTrue(hasPassages, "Generated maze should contain passages.");
    }

    @Test
    public void testWallRemoval() throws GenerateException {
        Maze generatedMaze = generatorKraskala.generate(maze);
        for (int row = 1; row < generatedMaze.height() - 1; row++) {
            for (int col = 1; col < generatedMaze.width() - 1; col++) {
                Cell.Type type = generatedMaze.grid()[row][col].type();
                assertTrue(type == PASSAGE || type == WALL, "Invalid cell type in generated maze.");
            }
        }
    }
}
