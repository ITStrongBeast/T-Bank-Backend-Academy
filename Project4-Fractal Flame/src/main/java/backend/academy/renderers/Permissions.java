package backend.academy.renderers;

import lombok.Getter;

@Getter
public enum Permissions {
    WSXGA("WSXGA: 1440 x 900", 1440, 900),
    FULLHD("FULL HD: 1920 x 1080", 1920, 1080),
    WUXGA("WUXGA: 1920 x 1200", 1920, 1200),
    WQXGA("WQXGA: 2560 x 1440", 2560, 1440),
    UHD("UHD: 3840 x 2160", 3840, 2160),
    UHD5K("UHD5K: 5120 x 2880", 5120, 2880);

    private final String names;
    private final int width;
    private final int height;

    Permissions(String name, int width, int height) {
        this.names = name;
        this.width = width;
        this.height = height;
    }
}
