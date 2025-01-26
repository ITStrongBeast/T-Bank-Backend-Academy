package backend.academy;

import static java.lang.Math.atan;
import static java.lang.Math.sqrt;

public record Point(double x, double y) {
    public double radius() {
        return sqrt(this.x * this.x + this.y * this.y);
    }

    public double theta() {
        if (this.y == 0) {
            return 0;
        }
        return atan(this.x / this.y);
    }
}
