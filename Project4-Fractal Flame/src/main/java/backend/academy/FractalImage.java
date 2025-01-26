package backend.academy;

public record FractalImage(Pixel[][] data, int width, int height) {

    @SuppressWarnings("MagicNumber")
    public static FractalImage create(int width, int height, boolean white) {
        Pixel[][] data = new Pixel[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                data[i][j] = white ? new Pixel(255, 255, 255, 0)
                : new Pixel(0, 0, 0, 0);
            }
        }
        return new FractalImage(data, width, height);
    }

    public boolean contains(int x, int y) {
        return 0 <= x && x < width && 0 <= y && y < height;
    }

    public synchronized Pixel pixel(int x, int y) {
        return data[y][x];
    }
}
