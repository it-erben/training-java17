package tech.erben.java17.textblocks.model;

public record TextBlockShowcase(
        String classicLiteral,
        String textBlockLiteral,
        boolean areEqual,
        String rawSql,
        String normalizedSql,
        String jsonPayload,
        String htmlSnippet,
        String rawEscapes,
        String translatedEscapes,
        String indentedMenu,
        String formattedTable
) {
}
