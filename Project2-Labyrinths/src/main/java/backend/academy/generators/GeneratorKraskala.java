package backend.academy.generators;

import backend.academy.Maze;
import backend.academy.exceptions.GenerateException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Класс реализующий генерацию лабиринта на основе алгоритма Краскала
 *
 * @version 1.0
 * @autor Георгий Голенков
 *     <a href="https://github.com/ITStrongBeast">...</a>
 */
public class GeneratorKraskala extends AbstractGenerator {
    /** Поле с лабиринтом */
    private Maze maze;

    @Override public Maze generate(Maze maze) throws GenerateException {
        this.maze = maze;

        List<GeneratorKraskala.Edge> edges = new ArrayList<>();
        GeneratorKraskala.UnionFind unionFind = new GeneratorKraskala.UnionFind(maze.height() * maze.width());

        for (int row = 1; row < maze.height() - 1; row++) {
            for (int col = 1; col < maze.width() - 1; col++) {
                if (row % 2 == 1 && col % 2 == 0 && col + 1 < maze.width() - 1) {
                    edges.add(new GeneratorKraskala.Edge(row, col, 0));
                }
                if (row % 2 == 0 && col % 2 == 1 && row + 1 < maze.height() - 1) {
                    edges.add(new GeneratorKraskala.Edge(row, col, 1));
                }
            }
        }

        Collections.shuffle(edges);

        int cell1;
        int cell2;
        for (GeneratorKraskala.Edge edge : edges) {
            if (edge.type == 1) {
                cell1 = (edge.row - 1) * maze.width() + edge.col;
                cell2 = (edge.row + 1) * maze.width() + edge.col;
            } else {
                cell1 = edge.row * maze.width() + edge.col + 1;
                cell2 = edge.row * maze.width() + edge.col - 1;
            }
            if (unionFind.find(cell1) != unionFind.find(cell2)) {
                removeWall(edge);
                unionFind.union(cell1, cell2);
            }
        }
        return maze;
    }

    /**
     * Метод, который создаёт проход вместо стены
     */
    private void removeWall(GeneratorKraskala.Edge edge) {
        for (int i = -1; i < 2; i++) {
            if (edge.type == 0) {
                maze.grid()[edge.row][edge.col + i].type(Maze.Cell.Type.PASSAGE);
            } else {
                maze.grid()[edge.row + i][edge.col].type(Maze.Cell.Type.PASSAGE);
            }
        }
    }

    /**
     * Вспомогательный класс в котором хранятся сведения о точке
     *
     * @version 1.0
     * @autor Георгий Голенков
     *     <a href="https://github.com/ITStrongBeast">...</a>
     */
    private static class Edge {
        int row;
        int col;
        int type;

        private Edge(int row, int col, int type) {
            this.row = row;
            this.col = col;
            this.type = type;
        }
    }

    /**
     * Вспомогательный класс с реализацией структуры непересекающихся множеств
     * с ранговой эвристикой для корректной работы алгоритма генерации
     *
     * @version 1.0
     * @autor Георгий Голенков
     *     <a href="https://github.com/ITStrongBeast">...</a>
     */
    private static class UnionFind {
        private final int[] parent;
        private final int[] rank;

        UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }

        /**
         * Метод находит множество к которому принадлежит точка
         */
        public int find(int p) {
            if (parent[p] != p) {
                parent[p] = find(parent[p]);
            }
            return parent[p];
        }

        /**
         * Метод для объединения двух множеств с использованием эвристики
         */
        public void union(int p, int q) {
            int rootP = find(p);
            int rootQ = find(q);

            if (rootP != rootQ) {
                if (rank[rootP] > rank[rootQ]) {
                    parent[rootQ] = rootP;
                } else if (rank[rootP] < rank[rootQ]) {
                    parent[rootP] = rootQ;
                } else {
                    parent[rootQ] = rootP;
                    rank[rootP]++;
                }
            }
        }
    }
}
