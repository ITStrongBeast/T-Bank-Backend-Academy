package backend.academy.samples.generators;

import backend.academy.Maze;
import backend.academy.generators.GeneratorPrima;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static backend.academy.Maze.Cell.Type.PASSAGE;
import static backend.academy.Maze.Cell.Type.WALL;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GeneratorPrimaTest {
    private GeneratorPrima generatorPrima;
    private Maze maze;

    @BeforeEach
    public void setUp() {
        generatorPrima = new GeneratorPrima();

        Maze.Cell[][] grid = new Maze.Cell[5][5];
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                grid[row][col] = new Maze.Cell(new Maze.Coordinate(row, col), WALL);
            }
        }

        maze = new Maze(grid);
        maze.width(4);
        maze.height(4);
        maze.start(new Maze.Coordinate(0, 1));
        maze.end(new Maze.Coordinate(4, 1));
    }

    @Test
    public void testGenerateMaze() {
        Maze generatedMaze = generatorPrima.generate(maze);

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
    public void testWallRemoval() {
        Maze generatedMaze = generatorPrima.generate(maze);
        for (int row = 1; row < generatedMaze.height() - 1; row++) {
            for (int col = 1; col < generatedMaze.width() - 1; col++) {
                Maze.Cell.Type type = generatedMaze.grid()[row][col].type();
                assertTrue(type == PASSAGE || type == WALL, "Invalid cell type in generated maze.");
            }
        }
    }
}
