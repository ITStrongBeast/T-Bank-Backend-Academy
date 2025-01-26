package backend.academy;

import backend.academy.exceptions.FindException;
import backend.academy.exceptions.GenerateException;
import backend.academy.exceptions.PositionException;
import backend.academy.generators.Generator;
import backend.academy.generators.GeneratorKraskala;
import backend.academy.generators.GeneratorPrima;
import backend.academy.generators.GeneratorPrimaModified;
import backend.academy.render.AdvancedRenderer;
import backend.academy.render.BaseRenderer;
import backend.academy.render.Renderer;
import backend.academy.seekers.AStarSolver;
import backend.academy.seekers.BFSSolver;
import backend.academy.seekers.Solver;
import java.io.IOException;
import java.util.Scanner;

/**
 * Класс обеспечивающий общую работу приложения.
 *
 * @version 1.0
 * @autor Георгий Голенков
 *     <a href="https://github.com/ITStrongBeast">...</a>
 */
@SuppressWarnings({"checkstyle:RegexpSinglelineJava"})
public class Mediator {

    /**
     * Метод, который запускает приложение и получает все необходимые данные от пользователя
     */
    public void run(Scanner scanner) throws IOException, PositionException, GenerateException, FindException {
        int[] size = requestSize(scanner);

        Maze.Coordinate[] entrances = requestEntrances(scanner, size);

        Generator generator = creatGeneratorFromInput(scanner);

        System.out.println("""
            Если вы ходите наполнить лабиринт различными поверхностями напишите yes:
            """);
        String isSurface = scanner.nextLine().toLowerCase();

        Maze maze = generator.generate(size[0], size[1], entrances[0], entrances[1], "yes".equals(isSurface));

        Renderer renderer = createRendererFromInput(scanner);

        System.out.println(renderer.render(maze));

        System.out.println("Найти решение (write: find)");
        String find = scanner.nextLine().toLowerCase();
        if (!"find".equals(find)) {
            return;
        }

        Solver solver = createSolverFromInput(scanner);

        System.out.println(renderer.render(maze, solver.solve(maze)));
    }

    /**
     * Метод для выбора пользователем алгоритма решения лабиринта
     */
    private Solver createSolverFromInput(Scanner scanner) {
        Solver solver = new BFSSolver();

        System.out.println("""
            Выберите алгоритм поиска пути от A до B в лабиринте (по дефолту используется алгоритм BFS):
             * BFS (bfs)
             * A-star (astar)
            """);

        String seeker = scanner.nextLine().toLowerCase();
        if ("astar".equals(seeker)) {
            solver = new AStarSolver();
        }
        return solver;
    }

    /**
     * Метод для выбора пользователем типы вывода лабиринта
     */
    private Renderer createRendererFromInput(Scanner scanner) {
        Renderer renderer = new BaseRenderer();

        System.out.println("""
            Выберите тип вывода лабиринта:
             * Base (base)
             * Advanced (adv)
            """);

        String render = scanner.nextLine().toLowerCase();
        if (render.equals("adv")) {
            renderer = new AdvancedRenderer();
        }
        return renderer;
    }

    /**
     * Метод, который создаёт генератор лабиринта основываясь на выборе пользователя
     */
    private Generator creatGeneratorFromInput(Scanner scanner) {
        Generator generator = new GeneratorKraskala();

        System.out.println("""
            Выберите алгоритм генерации лабиринта (по дефолту используется алгоритм Краскала):
             * алгоритм Краскала (Kraskala)
             * алгоритм Прима (Prima)
             * модифицированный алгоритм Прима (PrimaMod)
            """);
        scanner.nextLine();
        String prima = null;

        while (prima == null && scanner.hasNext()) {
            prima = scanner.nextLine().toLowerCase();
            if ("prima".equals(prima)) {
                generator = new GeneratorPrima();
            }
            if ("primamod".equals(prima)) {
                generator = new GeneratorPrimaModified();
            }
        }

        return generator;
    }

    /**
     * Метод, который принимает позиции точек входа/выхода
     */
    private Maze.Coordinate[] requestEntrances(Scanner scanner, int[] size) throws PositionException {
        Maze.Coordinate[] result = new Maze.Coordinate[2];

        System.out.println("Введите сторону(l-левая r-правая u-верхняя d-нижняя) и позицию (кроме углов) точки A: ");
        result[0] = requestPosition(scanner, size);

        System.out.println("Введите сторону(l-левая r-правая u-верхняя d-нижняя) и позицию (кроме углов) точки B: ");
        result[1] = requestPosition(scanner, size);

        if (result[0].row() == result[1].row() && Math.abs(result[0].col() - result[1].col()) < 2
            || result[0].col() == result[1].col() && Math.abs(result[0].row() - result[1].row()) < 2) {
            System.err.println("Вы установили точки A и B слишком близко друг к другу");
            throw new PositionException("Positions A and B are too close");
        }

        return result;
    }

    /**
     * Метод, который проводит валидацию позиций точек A и B
     */
    @SuppressWarnings({"checkstyle:CyclomaticComplexity"})
    private Maze.Coordinate requestPosition(Scanner scanner, int[] size) {
        scanner.nextLine();
        char side = ' ';
        int distance = 0;
        while (side == ' ' || distance == 0) {
            if (scanner.hasNext() && side == ' ') {
                side = scanner.next().toLowerCase().charAt(0);
                if (side != 'l' && side != 'r' && side != 'u' && side != 'd') {
                    side = ' ';
                    System.err.println("Вы ввели неверный символ");
                }
            }
            if (scanner.hasNextInt() && side != ' ') {
                distance = scanner.nextInt();
                if (distance < 1 || ((side == 'l' || side == 'r') && distance > size[0] - 2)
                    || ((side == 'u' || side == 'd') && distance > size[1] - 2)) {
                    distance = 0;
                    System.err.println("Вы ввели неверную позицию");
                }
            }
        }
        return switch (side) {
            case 'u' -> new Maze.Coordinate(0, distance);
            case 'd' -> new Maze.Coordinate(size[0] - 1, distance);
            case 'l' -> new Maze.Coordinate(distance, 0);
            case 'r' -> new Maze.Coordinate(distance, size[1] - 1);
            default -> new Maze.Coordinate(0, 1);
        };
    }

    /**
     * Метод, который принимает размеры лабиринта и производит их валидацию
     */
    @SuppressWarnings({"checkstyle:MagicNumber"})
    private int[] requestSize(Scanner scanner) {
        int[] size = new int[2];
        System.out.println("Введите размеры лабиринта: ");
        for (int i = 0; i < 2; i++) {
            while (size[i] == 0) {
                if (scanner.hasNextInt()) {
                    size[i] = scanner.nextInt();
                    if (size[i] < 3) {
                        System.err.println("Вы ввели некорректные данные");
                        size[i] = 0;
                    }
                }
            }
        }
        size[0] = size[0] % 2 == 0 ? size[0] + 1 : size[0];
        size[1] = size[1] % 2 == 0 ? size[1] + 1 : size[1];
        return size;
    }
}
