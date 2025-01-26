package backend.academy.samples.render;

import backend.academy.Maze;
import backend.academy.Maze.Coordinate;
import backend.academy.render.AdvancedRenderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static backend.academy.Maze.Cell.Type.PASSAGE;
import static backend.academy.Maze.Cell.Type.WALL;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AdvancedRendererTest {

    private AdvancedRenderer renderer;
    private Maze maze;

    @BeforeEach
    public void setUp() {
        renderer = new AdvancedRenderer();
        maze = createTestMaze(3, 3);
        maze.height(3);
        maze.width(3);
    }

    @Test
    public void testRenderMazeWithoutPath() {
        String expectedOutput = """
                ┌─┐
                │ │
                └─┘
                """;

        String renderedOutput = renderer.render(maze);
        assertEquals(expectedOutput.replace("\r\n", "\n"), renderedOutput.replace("\r\n", "\n"));
    }

    @Test
    public void testRenderMazeWithPath() {
        maze.start(new Maze.Coordinate(0, 1));
        maze.end(new Maze.Coordinate(2, 1));
        maze.grid()[0][1].type(Maze.Cell.Type.A);
        maze.grid()[2][1].type(Maze.Cell.Type.B);

        List<Coordinate> path = List.of(new Coordinate(1, 1));

        String expectedOutput = """
                │A│
                │■│
                │B│
                """;

        String renderedOutput = renderer.render(maze, path);
        assertEquals(expectedOutput.replace("\r\n", "\n"), renderedOutput.replace("\r\n", "\n"));
    }

    @Test
    public void testRenderWallSymbols() {
        char wallSymbol = renderer.render(maze).charAt(0);
        assertEquals('┌', wallSymbol);
    }

    @Test
    void testRenderMazeWithWalls() {
        Maze maze = createTestMaze(5, 5);
        maze.height(5);
        maze.width(5);
        maze.start(new Maze.Coordinate(0, 1));
        maze.end(new Maze.Coordinate(4, 3));
        maze.grid()[0][1].type(PASSAGE);
        maze.grid()[4][3].type(PASSAGE);
        maze.grid()[4][1].type(PASSAGE);

        maze.grid()[1][2].type(WALL);
        maze.grid()[2][1].type(WALL);
        maze.grid()[2][2].type(WALL);
        maze.grid()[2][3].type(WALL);

        String expectedOutput = """
            │ ┌─┐
            │ │ │
            ├─┴─┤
            │   │
            │ * │
            """;

        String renderedMaze = renderer.render(maze);
        assertEquals(expectedOutput.replace("\r\n", "\n"), renderedMaze.replace("\r\n", "\n"));
    }

    private Maze createTestMaze(int width, int height) {
        Maze.Cell[][] grid = new Maze.Cell[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = new Maze.Cell(new Maze.Coordinate(i, j), WALL);
            }
        }
        for (int row = 1; row < height - 1; row++) {
            for (int col = 1; col < width - 1; col++) {
                grid[row][col].type(PASSAGE);
            }
        }
        return new Maze(grid);
    }
}
