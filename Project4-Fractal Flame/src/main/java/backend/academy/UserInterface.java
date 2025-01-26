package backend.academy;

import backend.academy.multithreaded.MultithreadedCreatorFlame;
import backend.academy.renderers.ImageFormat;
import backend.academy.renderers.ImageUtils;
import backend.academy.renderers.Permissions;
import backend.academy.singlethreaded.CreatorFlame;
import backend.academy.transformations.Transformation;
import backend.academy.transformations.TransformationsFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class UserInterface {
    private final String tab = "    ";
    private final BufferedReader reader;
    private final PrintStream output;

    public UserInterface(InputStream input, PrintStream output) {
        this.reader = new BufferedReader(new InputStreamReader(input));
        this.output = output;
    }

    public void run() throws IOException {
        output.println("Здравствуйте, эта программа создана для генерации изображения фрактального пламени.");
        output.println("Введите параметры запуска генерации:");

        Permissions permissions = getPermissions();

        output.println("Если хотите белый фон введите white:");
        boolean white = Objects.equals(reader.readLine(), "white");

        FractalImage fractal = FractalImage.create(permissions.width(), permissions.height(), white);

        int iter = getIters();
        int symmetry = getSymmetry();

        List<Transformation> transformation = getTransformations();

        output.println("Если вы хотите запустить многопоточную генерацию напишите 'multi':");

        CreatorsFlame creator = new CreatorFlame();
        if (Objects.equals(reader.readLine(), "multi")) {
            creator = new MultithreadedCreatorFlame();
        }

        output.println("Подождите, генерация фрактала началась.");
        creator.create(fractal, iter, symmetry, transformation);
        output.println("Фрактал сгенерирован!");

        saveImage(fractal);
    }

    private void saveImage(FractalImage fractal) throws IOException {
        output.println("Введите путь и имя файла для сохранения:");
        //String path = reader.readLine();
        String path = "output";

        output.println("Введите формат файла:");
        for (ImageFormat f : ImageFormat.values()) {
            output.println(tab + f.name());
        }
        String strFormat = reader.readLine();

        ImageFormat format;
        try {
            format = ImageFormat.valueOf(strFormat.toUpperCase());
        } catch (IllegalArgumentException e) {
            format = ImageFormat.PNG;
        }

        ImageUtils.save(fractal, Path.of(path + "."), format);
        output.println("Изображение фрактала сохранено в файл: " + path + "." + format.val());
    }

    private int getSymmetry() throws IOException {
        output.println("Введите количество осей симметрии для генерации фрактала:");
        try {
            return Integer.parseInt(reader.readLine().split(" ")[0]);
        } catch (NumberFormatException e) {
            throw new IOException("Вы ввели некорректное значение количества осей симметрии");
        }
    }

    private int getIters() throws IOException {
        output.println("Введите количество итераций для генерации фрактала:");
        try {
            return Integer.parseInt(reader.readLine().split(" ")[0]);
        } catch (NumberFormatException e) {
            throw new IOException("Вы ввели некорректное значение количества итераций");
        }
    }

    private List<Transformation> getTransformations() throws IOException {
        output.println(
            "Выбирете несколько трансформационных функций (напишите имя функции и количество использований):");
        for (TransformationsFactory.Trans t : TransformationsFactory.Trans.values()) {
            output.println(tab + t.name());
        }
        output.println("Для окончания ввода напишите 'end'");
        Map<String, Integer> func = new HashMap<>();
        while (true) {
            String[] line = reader.readLine().split(" ");
            if (Objects.equals(line[0], "end")) {
                break;
            }
            if (line.length > 1) {
                func.put(line[0].toUpperCase(), Integer.parseInt(line[1]));
            }
        }
        return mapToList(func);
    }

    private List<Transformation> mapToList(Map<String, Integer> func) {
        List<Transformation> result = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : func.entrySet()) {
            String key = entry.getKey();
            int count = entry.getValue();
            try {
                for (int i = 0; i < count; i++) {
                    result.add(TransformationsFactory.getTrans(key));
                }
            } catch (IllegalArgumentException e) {
                System.err.printf("Warning: '%s' is not a valid Transformation enum.%n", key);
            }
        }

        return result;
    }

    private Permissions getPermissions() throws IOException {
        output.println("Выбирете одно из размеров изображения (напишите номер выбранного):");
        int i = 1;
        for (Permissions permissions : Permissions.values()) {
            output.println(tab + i + ") " + permissions.names());
            i++;
        }
        int result;
        try {
            result = Integer.parseInt(reader.readLine().split(" ")[0]);
        } catch (NumberFormatException e) {
            throw new IOException("Вы ввели некорректное значение");
        }
        return Permissions.values()[result - 1];
    }
}
