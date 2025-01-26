package backend.academy.renderers;

import lombok.Getter;

@Getter
public enum ImageFormat {
    JPEG("jpeg"),
    PNG("png"),
    SVG("svg");

    final String val;

    ImageFormat(String val) {
        this.val = val;
    }
}
