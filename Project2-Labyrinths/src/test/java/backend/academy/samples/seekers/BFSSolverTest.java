package backend.academy.samples.seekers;

import backend.academy.Maze;
import backend.academy.exceptions.FindException;
import backend.academy.seekers.BFSSolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BFSSolverTest {

    private BFSSolver solver;

    @BeforeEach
    void setUp() {
        solver = new BFSSolver();
    }

    @Test
    void testSolveSimpleMaze() throws FindException {
        Maze maze = createEmptyMaze(3, 3);
        maze.height(3);
        maze.width(3);
        maze.start(new Maze.Coordinate(0, 1));
        maze.end(new Maze.Coordinate(2, 1));
        maze.grid()[0][1].type(Maze.Cell.Type.PASSAGE);
        maze.grid()[2][1].type(Maze.Cell.Type.PASSAGE);

        List<Maze.Coordinate> path = solver.solve(maze);

        assertNotNull(path);
        assertFalse(path.isEmpty());
        assertEquals(new Maze.Coordinate(0, 1), path.getFirst());
        assertEquals(new Maze.Coordinate(2, 1), path.getLast());
    }

    @Test
    void testSolveWithObstacles() throws FindException {
        Maze maze = createEmptyMaze(5, 5);
        maze.height(5);
        maze.width(5);
        maze.start(new Maze.Coordinate(0, 1));
        maze.end(new Maze.Coordinate(4, 3));
        maze.grid()[0][1].type(Maze.Cell.Type.PASSAGE);
        maze.grid()[4][3].type(Maze.Cell.Type.PASSAGE);

        // Устанавливаем стены
        maze.grid()[2][1].type(Maze.Cell.Type.WALL);
        maze.grid()[2][2].type(Maze.Cell.Type.WALL);

        List<Maze.Coordinate> path = solver.solve(maze);

        assertNotNull(path);
        assertFalse(path.isEmpty());
        assertEquals(new Maze.Coordinate(0, 1), path.getFirst());
        assertEquals(new Maze.Coordinate(4, 3), path.getLast());
    }

    @Test
    void testNoPathAvailable() {
        Maze maze = createEmptyMaze(5, 5);
        maze.height(5);
        maze.width(5);
        maze.start(new Maze.Coordinate(0, 1));
        maze.end(new Maze.Coordinate(4, 3));
        maze.grid()[0][1].type(Maze.Cell.Type.PASSAGE);
        maze.grid()[4][3].type(Maze.Cell.Type.PASSAGE);

        maze.grid()[2][1].type(Maze.Cell.Type.WALL);
        maze.grid()[2][2].type(Maze.Cell.Type.WALL);
        maze.grid()[2][3].type(Maze.Cell.Type.WALL);

        assertThrows(FindException.class, () -> solver.solve(maze));
    }

    private Maze createEmptyMaze(int width, int height) {
        Maze.Cell[][] grid = new Maze.Cell[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < height; j++) {
                grid[i][j] = new Maze.Cell(new Maze.Coordinate(i, j), Maze.Cell.Type.WALL);
            }
        }
        for (int row = 1; row < height - 1; row++) {
            for (int col = 1; col < width - 1; col++) {
                grid[row][col].type(Maze.Cell.Type.PASSAGE);
            }
        }
        return new Maze(grid);
    }
}
