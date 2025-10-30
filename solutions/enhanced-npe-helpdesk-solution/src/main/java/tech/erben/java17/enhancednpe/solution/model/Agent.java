package tech.erben.java17.enhancednpe.solution.model;

public record Agent(
        String name,
        String email,
        String primaryColor
) {
    public static Agent fallback() {
        return new Agent("Unbesetzt", "support@gfu.de", "#9ca3af");
    }
}
