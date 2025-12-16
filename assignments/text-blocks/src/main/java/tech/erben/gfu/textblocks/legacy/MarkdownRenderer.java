package tech.erben.gfu.textblocks.legacy;

import java.util.List;

public class MarkdownRenderer {

    public String renderReleaseNotes(String version, List<String> features) {
        StringBuilder builder = new StringBuilder();
        builder.append("# Release ").append(version).append("\n\n");
        builder.append("## Änderungen").append("\n");

        for (String feature : features) {
            builder.append("- ").append(feature).append("\n");
        }

        builder.append("\n");
        builder.append("Vielen Dank fürs Aktualisieren!");
        return builder.toString();
    }

    public String renderApiDoc(String endpoint, String description, List<String> requiredFields) {
        StringBuilder builder = new StringBuilder();
        builder.append("### ").append(endpoint).append("\n");
        builder.append(description).append("\n\n");
        builder.append("**Required fields**").append("\n");

        for (String field : requiredFields) {
            builder.append("- ").append(field).append("\n");
        }

        builder.append("\nExample payload:\n");
        builder.append("{\n");
        builder.append("  \"name\": \"Rita\",\n");
        builder.append("  \"email\": \"rita@example.com\",\n");
        builder.append("  \"workshopId\": 42\n");
        builder.append("}\n");

        return builder.toString();
    }
}
