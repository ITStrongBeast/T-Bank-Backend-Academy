package backend.academy.samples;

import backend.academy.Point;
import backend.academy.transformations.AthenianTransformation;
import backend.academy.transformations.Transformation;
import backend.academy.Changer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChangerTest {
    private Transformation mockTransformation;
    private AthenianTransformation mockAthenianTransformation;

    @BeforeEach
    void setUp() {
        mockTransformation = mock(Transformation.class);
        mockAthenianTransformation = mock(AthenianTransformation.class);
    }

    @Test
    void constructor_shouldInitializeRgbArrayWithRandomValues() {
        Changer changer = new Changer(mockTransformation);
        int[] rgb = changer.rgb();
        assertThat(rgb).isNotNull();
        assertThat(rgb).hasSize(3);
        for (int value : rgb) {
            assertThat(value).isBetween(0, 255);
        }
    }

    @Test
    void change_shouldApplyAthenianAndNonlinearTransformations() {
        Changer changer = new Changer(mockTransformation);
        Point inputPoint = new Point(1, 1);
        Point transformedByAthenian = new Point(2, 2);
        Point finalTransformed = new Point(3, 3);

        when(mockAthenianTransformation.transform(inputPoint)).thenReturn(transformedByAthenian);
        when(mockTransformation.apply(transformedByAthenian)).thenReturn(finalTransformed);

        Point result = changer.change(inputPoint, mockAthenianTransformation);

        assertThat(result).isEqualTo(finalTransformed);
        verify(mockAthenianTransformation, times(1)).transform(inputPoint);
        verify(mockTransformation, times(1)).apply(transformedByAthenian);
    }

    @Test
    void change_shouldHandleNullAthenianTransformationGracefully() {
        Changer changer = new Changer(mockTransformation);
        Point inputPoint = new Point(1, 1);

        assertThatThrownBy(() -> changer.change(inputPoint, null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void change_shouldWorkWithRealAthenianAndNonlinearTransformations() {
        Transformation realNonlinear = point -> new Point(point.x() + 1, point.y() + 1);
        AthenianTransformation realAthenian = AthenianTransformation.create();
        Changer changer = new Changer(realNonlinear);
        Point inputPoint = new Point(1, 1);

        Point result = changer.change(inputPoint, realAthenian);

        assertThat(result).isNotNull();
    }
}
