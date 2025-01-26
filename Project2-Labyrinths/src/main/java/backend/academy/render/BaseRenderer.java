package backend.academy.render;

/**
 * Класс представления лабиринта в виде строки с использованием стандартных символов
 *
 * @version 1.0
 * @autor Георгий Голенков
 * <a href="https://github.com/ITStrongBeast">...</a>
 */
public class BaseRenderer extends AbstractRenderer {
    /**
     * Метод, который превращает каждую точку лабиринта в символ в зависимости от её типа
     */
    protected String renderAll() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < maze.height(); i++) {
            for (int j = 0; j < maze.width(); j++) {
                result.append(grid[i][j].type().symbol());
            }
            result.append(System.lineSeparator());
        }
        return result.toString();
    }
}
