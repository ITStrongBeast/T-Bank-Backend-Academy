package backend.academy.samples.render;

import backend.academy.Maze;
import backend.academy.render.BaseRenderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BaseRendererTest {

    private BaseRenderer renderer;

    @BeforeEach
    void setUp() {
        renderer = new BaseRenderer();
    }

    @Test
    void testRenderMazeWithoutPath() {
        Maze maze = createTestMaze(3, 3);
        maze.height(3);
        maze.width(3);
        String expectedOutput = """
            %%%
            % %
            %%%
            """;

        String renderedMaze = renderer.render(maze);
        assertEquals(expectedOutput.replace("\r\n", "\n"), renderedMaze.replace("\r\n", "\n"));
    }

    @Test
    void testRenderMazeWithWalls() {
        Maze maze = createTestMaze(5, 5);
        maze.height(5);
        maze.width(5);
        maze.start(new Maze.Coordinate(0, 1));
        maze.end(new Maze.Coordinate(4, 3));
        maze.grid()[0][1].type(Maze.Cell.Type.A);
        maze.grid()[4][3].type(Maze.Cell.Type.B);

        maze.grid()[2][1].type(Maze.Cell.Type.WALL);
        maze.grid()[2][2].type(Maze.Cell.Type.WALL);
        maze.grid()[2][3].type(Maze.Cell.Type.WALL);

        String expectedOutput = """
            %A%%%
            %   %
            %%%%%
            %   %
            %%%B%
            """;

        String renderedMaze = renderer.render(maze);
        assertEquals(expectedOutput.replace("\r\n", "\n"), renderedMaze.replace("\r\n", "\n"));
    }

    @Test
    void testRenderMazeWithPath() {
        Maze maze = createTestMaze(5, 5);
        maze.height(5);
        maze.width(5);
        maze.start(new Maze.Coordinate(0, 1));
        maze.end(new Maze.Coordinate(4, 3));
        maze.grid()[0][1].type(Maze.Cell.Type.A);
        maze.grid()[4][3].type(Maze.Cell.Type.B);

        maze.grid()[2][1].type(Maze.Cell.Type.WALL);
        maze.grid()[2][2].type(Maze.Cell.Type.WALL);

        List<Maze.Coordinate> path = Arrays.asList(
            new Maze.Coordinate(0, 1),
            new Maze.Coordinate(1, 1),
            new Maze.Coordinate(1, 2),
            new Maze.Coordinate(1, 3),
            new Maze.Coordinate(2, 3),
            new Maze.Coordinate(3, 3),
            new Maze.Coordinate(3, 3)
        );

        String expectedOutput = """
            %■%%%
            %■■■%
            %%%■%
            %  ■%
            %%%B%
            """;

        String renderedMaze = renderer.render(maze, path);
        assertEquals(expectedOutput.replace("\r\n", "\n"), renderedMaze.replace("\r\n", "\n"));
    }

    private Maze createTestMaze(int width, int height) {
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
