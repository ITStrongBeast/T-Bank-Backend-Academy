package backend.academy.transformations;

import backend.academy.Point;
import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public class TransformationsFactory {
    private TransformationsFactory() {
    }

    @SuppressWarnings("MagicNumber")
    public enum Trans {
        LINEAR(point -> point),

        SINUSOIDAL(point -> new Point(sin(point.x()), sin(point.y()))),

        SPHERICAL(point -> new Point(1 / (point.radius() * point.radius()) * point.x(),
            1 / (point.radius() * point.radius()) * point.y())),

        SWIRL(point -> {
            double sin = sin(point.radius() * point.radius());
            double cos = cos(point.radius() * point.radius());
            return new Point(point.x() * sin - point.y() * cos, point.x() * cos + point.y() * sin);
        }),

        HORSESHOE(point -> new Point(1 / point.radius() * (point.x() - point.y()) * (point.x() + point.y()),
            1 / point.radius() * 2 * point.x() * point.y())),

        POLAR(point -> new Point(point.theta() / PI, point.radius() - 1)),

        HANDKERCHIEF(point -> new Point(point.radius() * sin(point.theta() + point.radius()),
            point.radius() * cos(point.theta() - point.radius()))),

        HEART(point -> new Point(point.radius() * sin(point.theta() * point.radius()),
            -1 * point.radius() * cos(point.theta() * point.radius()))),

        DISC(point -> new Point(point.theta() / PI * sin(PI * point.radius()),
            point.theta() / PI * cos(PI * point.radius()))),

        SPIRAL(point -> new Point(1 / point.radius() * (cos(point.theta()) + sin(point.radius())),
            1 / point.radius() * (sin(point.theta()) - cos(point.radius())))),

        HYPERBOLIC(point -> new Point(sin(point.theta()) / point.radius(), point.radius() * cos(point.theta()))),

        DIAMOND(point -> new Point(sin(point.theta()) * cos(point.radius()),
            cos(point.theta()) * sin(point.radius()))),

        FISHEYE(point -> {
            double k = 2 / (point.radius() + 1);
            return new Point(k * point.y(), k * point.x());
        }),

        EXPONENTIAL(point -> {
            double e = Math.exp(point.x() - 1);
            return new Point(e * cos(PI * point.y()), e * sin(PI * point.y()));
        }),

        COSINE(point -> new Point(cos(PI * point.x()) * Math.cosh(point.y()),
            -sin(PI * point.x()) * Math.sinh(point.y()))),

        EYEFISH(point -> {
            double k = 2 / (point.radius() + 1);
            return new Point(k * point.x(), k * point.y());
        }),

        BUBBLE(point -> {
            double k = 4 / (point.radius() * point.radius() + 4);
            return new Point(k * point.y(), k * point.x());
        }),

        CYLINDER(point -> new Point(sin(point.x()), point.y())),

        TANGENT(point -> new Point(sin(point.x()) / cos(point.y()), Math.tan(point.y()))),

        CROSS(point -> {
            double d = point.x() * point.x() - point.y() * point.y();
            double k = sqrt(1 / d * d);
            return new Point(k * point.x(), k * point.y());
        });

        private final Transformation val;

        Trans(Transformation value) {
            this.val = value;
        }

        private Transformation val() {
            return val;
        }
    }

    public static Transformation getTrans(String name) {
        return Trans.valueOf(name.toUpperCase()).val();
    }
}
