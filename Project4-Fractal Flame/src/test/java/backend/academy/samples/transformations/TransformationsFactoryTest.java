package backend.academy.samples.transformations;

import backend.academy.Point;
import backend.academy.transformations.TransformationsFactory;
import backend.academy.transformations.Transformation;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TransformationsFactoryTest {

    @Test
    void getTrans_shouldReturnCorrectTransformationForLINEAR() {
        String name = "LINEAR";
        Point input = new Point(1, 1);

        Transformation transformation = TransformationsFactory.getTrans(name);
        Point result = transformation.apply(input);

        assertThat(result).isEqualTo(input);
    }

    @Test
    void getTrans_shouldReturnCorrectTransformationForSINUSOIDAL() {
        String name = "SINUSOIDAL";
        Point input = new Point(Math.PI / 2, Math.PI / 2);

        Transformation transformation = TransformationsFactory.getTrans(name);
        Point result = transformation.apply(input);

        assertThat(result.x()).isCloseTo(1.0, within(1e-5));
        assertThat(result.y()).isCloseTo(1.0, within(1e-5));
    }

    @Test
    void getTrans_shouldReturnCorrectTransformationForSPHERICAL() {
        String name = "SPHERICAL";
        Point input = new Point(2, 2);

        Transformation transformation = TransformationsFactory.getTrans(name);
        Point result = transformation.apply(input);

        double radiusSquared = input.radius() * input.radius();
        assertThat(result.x()).isCloseTo(input.x() / radiusSquared, within(1e-5));
        assertThat(result.y()).isCloseTo(input.y() / radiusSquared, within(1e-5));
    }

    @Test
    void getTrans_shouldThrowExceptionForInvalidName() {
        String invalidName = "INVALID";

        assertThatThrownBy(() -> TransformationsFactory.getTrans(invalidName))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("No enum constant");
    }

    @Test
    void getTrans_shouldHandleAllTransformations() {
        Point input = new Point(1, 1);
        for (TransformationsFactory.Trans transformation : TransformationsFactory.Trans.values()) {
            Transformation trans = TransformationsFactory.getTrans(transformation.name());
            Point result = trans.apply(input);
            assertThat(result).isNotNull();
        }
    }
}
