package backend.academy.transformations;

import backend.academy.Point;
import java.util.Random;

public class AthenianTransformation {
    private final double a;
    private final double b;
    private final double c;
    private final double d;
    private final double e;
    private final double f;

    private AthenianTransformation(double a, double b, double c, double d, double e, double f) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
    }

    public static AthenianTransformation create() {
        Random random = new Random();
        double a;
        double b;
        double e;
        double d;
        double c = -1 + random.nextDouble() * 2;
        double f = -1 + random.nextDouble() * 2;

        do {
            a = -1 + random.nextDouble() * 2;
            b = -1 + random.nextDouble() * 2;
            d = -1 + random.nextDouble() * 2;
            e = -1 + random.nextDouble() * 2;
        } while (!isValid(a, b, d, e));

        return new AthenianTransformation(a, b, c, d, e, f);
    }

    private static boolean isValid(double a, double b, double d, double e) {
        return a * a + d * d < 1
            && b * b + e * e < 1
            && a * a + b * b + d * d + e * e < 1 + (a * e - b * d) * (a * e - b * d);
    }

    public Point transform(Point point) {
        return new Point(a * point.x() + b * point.y() + c, d * point.x() + e * point.y() + f);
    }
}
