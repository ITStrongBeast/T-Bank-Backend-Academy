package backend.academy.render;

import backend.academy.Maze;
import java.util.List;

public abstract class AbstractRenderer implements Renderer {
    /** Поле всех точек лабиринта */
    protected Maze.Cell[][] grid;
    /** Поле лабиринта */
    protected Maze maze;

    @Override
    public String render(Maze maze) {
        this.grid = maze.grid();
        this.maze = maze;
        return renderAll();
    }

    @Override
    public String render(Maze maze, List<Maze.Coordinate> path) {
        this.grid = maze.grid();
        this.maze = maze;
        for (Maze.Coordinate pair : path) {
            grid[pair.row()][pair.col()].type(Maze.Cell.Type.WAY);
        }
        return renderAll();
    }

    protected abstract String renderAll();
}
