package backend.academy;

import backend.academy.transformations.AthenianTransformation;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class Creator {

    private final FractalImage fractal;
    private final Changer changer;
    private final Set<AthenianTransformation> transform;
    private Point point;
    private final int numberIter;
    private final int numberSymmetry;
    public double xMax = 1;
    public double xMin = -1;
    public double yMax = 1;
    public double yMin = -1;

    public Creator(
        FractalImage fractal, Changer changer,
        Set<AthenianTransformation> transform, int numberIter, int numberSymmetry
    ) {
        this.fractal = fractal;
        this.changer = changer;
        this.transform = transform;
        this.numberIter = numberIter;
        this.numberSymmetry = numberSymmetry;
        if (fractal.width() > fractal.height()) {
            this.xMax = ((double) fractal.width()) / ((double) fractal.height());
            this.xMin = -xMax;
        } else {
            this.yMax = ((double) fractal.height()) / ((double) fractal.width());
            this.yMin = -yMax;
        }
    }

    @SuppressWarnings("MagicNumber")
    public void create() {
        int n = 10000;
        for (int j = 0; j < n; j++) {
            point = getStartPoint();
            for (int i = -20; i < numberIter; i++) {
                point = changer.change(point, randomAthenian());
                if (i <= 0) {
                    continue;
                }
                symmetry();
            }
        }
    }

    private AthenianTransformation randomAthenian() {
        Random random = new Random();
        int randomIndex = random.nextInt(transform.size());
        Iterator<AthenianTransformation> iterator = transform.iterator();
        AthenianTransformation randomElement = null;
        for (int k = 0; k <= randomIndex; k++) {
            randomElement = iterator.next();
        }
        return randomElement;
    }

    private void symmetry() {
        double theta2 = 0.0;
        for (int s = 0; s < numberSymmetry; theta2 += Math.PI * 2 / numberSymmetry, s++) {
            Point p = rotate(theta2);
            visiting(p);
        }
    }

    private Point rotate(double theta) {
        double cosTheta = Math.cos(theta);
        double sinTheta = Math.sin(theta);
        double x = point.x() * cosTheta - point.y() * sinTheta;
        double y = point.x() * sinTheta + point.y() * cosTheta;
        return new Point(x, y);
    }

    private void visiting(Point p) {
        Pixel pixel = getNormalPoint(p);
        if (pixel == null) {
            return;
        }
        int h = pixel.hitCount() == 0 ? 0 : 1;
        int l = pixel.hitCount() == 0 ? 1 : 0;
        pixel.r((pixel.r() * h + changer.rgb[0]) / 2 + l * (pixel.r() * h + changer.rgb[0]) / 2);
        pixel.g((pixel.g() * h + changer.rgb[1]) / 2 + l * (pixel.g() * h + changer.rgb[1]) / 2);
        pixel.b((pixel.b() * h + changer.rgb[2]) / 2 + l * (pixel.b() * h + changer.rgb[2]) / 2);
        pixel.hitCount(pixel.hitCount() + 1);
    }

    private Pixel getNormalPoint(Point p) {
        int x = (int) (fractal.width() - ((xMax - p.x()) / (xMax - xMin)) * fractal.width());
        int y = (int) (fractal.height() - ((yMax - p.y()) / (yMax - yMin)) * fractal.height());
        if (fractal.contains(x, y)) {
            return fractal.pixel(x, y);
        }
        return null;
    }

    private Point getStartPoint() {
        Random random = new Random();
        return new Point(xMin + (xMax - xMin) * random.nextDouble(), -1 + random.nextDouble() * 2);
    }
}
