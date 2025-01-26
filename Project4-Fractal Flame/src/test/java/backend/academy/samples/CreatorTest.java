package backend.academy.samples;

import backend.academy.Changer;
import backend.academy.Creator;
import backend.academy.FractalImage;
import backend.academy.Pixel;
import backend.academy.Point;
import backend.academy.transformations.AthenianTransformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreatorTest {

    private FractalImage mockFractal;
    private Changer mockChanger;
    private Set<AthenianTransformation> mockTransformations;

    @BeforeEach
    void setUp() {
        mockFractal = mock(FractalImage.class);
        mockChanger = mock(Changer.class);
        mockTransformations = new HashSet<>();
        mockTransformations.add(mock(AthenianTransformation.class));
    }

    @Test
    void getNormalPoint_shouldReturnCorrectPixelForValidPointWithReflection() throws Exception {
        when(mockFractal.width()).thenReturn(800);
        when(mockFractal.height()).thenReturn(600);
        when(mockFractal.contains(anyInt(), anyInt())).thenReturn(true);
        Pixel mockPixel = mock(Pixel.class);
        when(mockFractal.pixel(anyInt(), anyInt())).thenReturn(mockPixel);

        Creator creator = new Creator(mockFractal, mockChanger, mockTransformations, 10, 6);
        Point point = new Point(0.5, 0.5);

        Method getNormalPointMethod = Creator.class.getDeclaredMethod("getNormalPoint", Point.class);
        getNormalPointMethod.setAccessible(true);

        Pixel result = (Pixel) getNormalPointMethod.invoke(creator, point);

        assertThat(result).isEqualTo(mockPixel);
        verify(mockFractal).contains(anyInt(), anyInt());
        verify(mockFractal).pixel(anyInt(), anyInt());
    }

    @Test
    void randomAthenian_shouldReturnRandomElementFromSetWithReflection() throws Exception {
        AthenianTransformation transformation1 = mock(AthenianTransformation.class);
        AthenianTransformation transformation2 = mock(AthenianTransformation.class);
        mockTransformations.add(transformation1);
        mockTransformations.add(transformation2);

        Creator creator = new Creator(mockFractal, mockChanger, mockTransformations, 10, 6);

        Method randomAthenianMethod = Creator.class.getDeclaredMethod("randomAthenian");
        randomAthenianMethod.setAccessible(true);

        AthenianTransformation result = (AthenianTransformation) randomAthenianMethod.invoke(creator);
        assertThat(mockTransformations).contains(result);
    }
}
