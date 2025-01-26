package backend.academy.samples.renderers;

import backend.academy.FractalImage;
import backend.academy.Pixel;
import backend.academy.renderers.ImageUtils;
import backend.academy.renderers.ImageFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImageUtilsTest {

    private FractalImage mockImage;

    @BeforeEach
    void setUp() {
        mockImage = mock(FractalImage.class);
    }

    @Test
    void save_shouldSaveImageToFile() throws IOException {
        int width = 2;
        int height = 2;
        Pixel[][] pixels = {
            {new Pixel(10, 20, 30, 1), new Pixel(40, 50, 60, 2)},
            {new Pixel(70, 80, 90, 3), new Pixel(100, 110, 120, 4)}
        };
        when(mockImage.data()).thenReturn(pixels);
        when(mockImage.width()).thenReturn(width);
        when(mockImage.height()).thenReturn(height);
        Path tempPath = Path.of("testImage");
        ImageFormat format = ImageFormat.PNG;

        ImageUtils.save(mockImage, tempPath, format);

        File savedFile = new File(tempPath + format.val());
        assertThat(savedFile).exists();
        assertThat(savedFile.getName()).endsWith(format.val());
    }

    @Test
    void createImageFromArray_shouldReturnBufferedImage() throws Exception {
        int width = 2;
        int height = 2;
        Pixel[][] pixels = {
            {new Pixel(255, 0, 0, 1), new Pixel(0, 255, 0, 1)},
            {new Pixel(0, 0, 255, 1), new Pixel(255, 255, 255, 1)}
        };

        var method = ImageUtils.class.getDeclaredMethod("createImageFromArray", Pixel[][].class, int.class, int.class);
        method.setAccessible(true);

        BufferedImage result = (BufferedImage) method.invoke(null, pixels, width, height);

        assertThat(result).isNotNull();
        assertThat(result.getWidth()).isEqualTo(width);
        assertThat(result.getHeight()).isEqualTo(height);
    }

    @Test
    void correction_shouldAdjustPixelValues() throws Exception {
        int width = 2;
        int height = 2;
        Pixel[][] pixels = {
            {new Pixel(100, 150, 200, 2), new Pixel(120, 180, 240, 4)},
            {new Pixel(140, 210, 255, 8), new Pixel(160, 240, 255, 16)}
        };
        var method = ImageUtils.class.getDeclaredMethod("correction", Pixel[][].class, int.class, int.class);
        method.setAccessible(true);
        method.invoke(null, pixels, width, height);
        for (Pixel[] row : pixels) {
            for (Pixel pixel : row) {
                assertThat(pixel.r()).isBetween(0, 255);
                assertThat(pixel.g()).isBetween(0, 255);
                assertThat(pixel.b()).isBetween(0, 255);
            }
        }
    }

    @Test
    void save_shouldThrowExceptionForInvalidPath() {
        when(mockImage.data()).thenReturn(new Pixel[1][1]);
        when(mockImage.width()).thenReturn(1);
        when(mockImage.height()).thenReturn(1);
        Path invalidPath = Path.of("/invalid/path");
        ImageFormat format = ImageFormat.PNG;
        assertThatThrownBy(() -> ImageUtils.save(mockImage, invalidPath, format))
            .isInstanceOf(NullPointerException.class);
    }
}

