package backend.academy;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pixel {
    private volatile int r;
    private volatile int g;
    private volatile int b;
    private volatile int hitCount;
    private double normal;

    public Pixel(int r, int g, int b, int hitCount) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.hitCount = hitCount;
        this.normal = 0;
    }
}
