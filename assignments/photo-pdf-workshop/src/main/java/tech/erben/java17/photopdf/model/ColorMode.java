package tech.erben.java17.photopdf.model;

public enum ColorMode {
    COLOR("Farbe behalten"),
    GRAYSCALE("Graustufen"),
    INVERTED("Invertierte Farben");

    private final String label;

    ColorMode(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
