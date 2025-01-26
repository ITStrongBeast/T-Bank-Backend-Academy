package backend.academy.samples.transformations;

import backend.academy.Point;
import backend.academy.transformations.AthenianTransformation;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class AthenianTransformationTest {

    @Test
    void create_shouldGenerateValidTransformation() {
        AthenianTransformation transformation = AthenianTransformation.create();
        assertThat(transformation).isNotNull();
    }

    @Test
    void transform_shouldApplyTransformationCorrectly() {
        AthenianTransformation transformation = AthenianTransformation.create();
        Point inputPoint = new Point(1, 1);
        Point result = transformation.transform(inputPoint);
        assertThat(result).isNotNull();
        assertThat(result.x()).isNotNaN();
        assertThat(result.y()).isNotNaN();
    }

    @Test
    void isValid_shouldReturnTrueForValidParameters() {
        double a = 0.5;
        double b = 0.5;
        double d = 0.3;
        double e = 0.3;
        boolean result = isValidForTest(a, b, d, e);
        assertThat(result).isTrue();
    }

    @Test
    void isValid_shouldReturnFalseForInvalidParameters() {
        double a = 1.5;
        double b = 1.5;
        double d = 1.3;
        double e = 1.3;
        boolean result = isValidForTest(a, b, d, e);
        assertThat(result).isFalse();
    }

    private boolean isValidForTest(double a, double b, double d, double e) {
        try {
            var method = AthenianTransformation.class.getDeclaredMethod("isValid", double.class, double.class, double.class, double.class);
            method.setAccessible(true);
            return (boolean) method.invoke(null, a, b, d, e);
        } catch (Exception e1) {
            throw new RuntimeException(e1);
        }
    }
}

