package backend.academy.renderers;

import backend.academy.FractalImage;
import backend.academy.Pixel;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import static java.lang.Math.log10;
import static java.lang.Math.pow;

@SuppressWarnings("MagicNumber")
public final class ImageUtils {
    private ImageUtils() {
    }

    public static void save(FractalImage image, Path filename, ImageFormat format) throws IOException {
        correction(image.data(), image.width(), image.height());
        BufferedImage images = createImageFromArray(image.data(), image.width(), image.height());
        File outputFile = new File(filename.toString() + format.val());
        ImageIO.write(images, format.val(), outputFile);
    }

    private static BufferedImage createImageFromArray(Pixel[][] pixels, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int red = pixels[y][x].r();
                int green = pixels[y][x].g();
                int blue = pixels[y][x].b();
                image.setRGB(x, y, (red << 16) | (green << 8) | blue);
            }
        }
        return image;
    }

    private static void correction(Pixel[][] pixels, int width, int height) {
        double max = 0.0;
        double gamma = 2.2;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (pixels[row][col].hitCount() == 0) {
                    continue;
                }
                pixels[row][col].normal(log10(pixels[row][col].hitCount()));
                if (pixels[row][col].normal() > max) {
                    max = pixels[row][col].normal();
                }
            }
        }
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (pixels[row][col].r() == 255 && pixels[row][col].g() == 255 && pixels[row][col].b() == 255) {
                    continue;
                }
                pixels[row][col].normal(pixels[row][col].normal() / max);
                pixels[row][col].r((int) (pixels[row][col].r() * pow(pixels[row][col].normal(), (1.0 / gamma))));
                pixels[row][col].g((int) (pixels[row][col].g() * pow(pixels[row][col].normal(), (1.0 / gamma))));
                pixels[row][col].b((int) (pixels[row][col].b() * pow(pixels[row][col].normal(), (1.0 / gamma))));
            }
        }
    }
}
