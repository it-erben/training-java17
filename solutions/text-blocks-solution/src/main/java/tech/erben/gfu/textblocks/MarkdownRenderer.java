package tech.erben.gfu.textblocks;

import java.util.List;
import java.util.stream.Collectors;

public class MarkdownRenderer {

    public String renderReleaseNotes(String version, List<String> features) {
        String featureLines = features.stream()
                .map(feature -> "- " + feature)
                .collect(Collectors.joining("\n"));

        String statusTable = """
                | Bereich | Status |
                | ------- | ------ |
                | Release | live   |
                """.stripIndent();

        return """
                # Release %s

                ## Änderungen
                %s

                %s

                Vielen Dank fürs Aktualisieren!
                """.stripIndent().formatted(version, featureLines, statusTable);
    }

    public String renderApiDoc(String endpoint, String description, List<String> requiredFields) {
        String required = requiredFields.stream()
                .map(field -> "- " + field)
                .collect(Collectors.joining("\n"));

        String examplePayload = """
                {
                  "name": "Rita",
                  "email": "rita@example.com",
                  "workshopId": 42
                }
                """.stripIndent();

        return """
                ### %s
                %s

                **Required fields**
                %s

                Example payload:
                %s
                """.stripIndent().formatted(endpoint, description, required, examplePayload);
    }
}
