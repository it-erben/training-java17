package tech.erben.java17.photopdf.model;

import java.util.Objects;

public final class PrintOptions {
    private final ColorMode colorMode;
    private final String requestedBy;
    private final String annotation;

    public PrintOptions(ColorMode colorMode, String requestedBy, String annotation) {
        if (colorMode == null) {
            colorMode = ColorMode.COLOR;
        }
        this.colorMode = colorMode;
        this.requestedBy = requestedBy;
        this.annotation = annotation;
    }

    public ColorMode colorMode() {
        return colorMode;
    }

    public String requestedBy() {
        return requestedBy;
    }

    public String annotation() {
        return annotation;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (PrintOptions) obj;
        return Objects.equals(this.colorMode, that.colorMode) &&
            Objects.equals(this.requestedBy, that.requestedBy) &&
            Objects.equals(this.annotation, that.annotation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(colorMode, requestedBy, annotation);
    }

    @Override
    public String toString() {
        return "PrintOptions[" +
            "colorMode=" + colorMode + ", " +
            "requestedBy=" + requestedBy + ", " +
            "annotation=" + annotation + ']';
    }

}
