package backend.academy;

import backend.academy.transformations.AthenianTransformation;
import backend.academy.transformations.Transformation;
import java.util.Random;
import lombok.Getter;

@SuppressWarnings("MagicNumber")
public class Changer {
    private final Transformation nonlinear;
    @Getter final int[] rgb;

    public Changer(Transformation nonlinear) {
        this.nonlinear = nonlinear;
        this.rgb = new int[3];
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            rgb[i] = random.nextInt(256);
        }
    }

    public Point change(Point point, AthenianTransformation athenian) {
        return nonlinear.apply(athenian.transform(point));
    }
}
