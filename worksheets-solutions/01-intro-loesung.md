# Musterlösung: Patent-Export (Java 17 Features)

```java
import java.util.Map;
import java.util.stream.Collectors;

public class PatentExport {
    public static void main(String[] args) {
        // 1. Statische Daten (Map.of) & var
        var patents = Map.of(
            "DE102024", Map.of(
                "de_DE", "Faltbares Display",
                "en_US", "Foldable Display",
                "fr_FR", "Écran pliable"
            ),
            "EP305011", Map.of(
                "de_DE", "Langlebiger Akku",
                "en_US", "Long-life battery",
                "fr_FR", "Batterie longue durée"
            ),
            "US998877", Map.of(
                "de_DE", "KI Chip",
                "en_US", "AI Chip",
                "fr_FR", "Puce IA"
            )
        );

        // Stream Verarbeitung für JSON Generierung
        var jsonOutput = patents.entrySet().stream()
            .map(entry -> {
                var id = entry.getKey();
                
                // 2. Switch Expression
                var gebuehr = switch (id.substring(0, 2)) {
                    case "DE" -> 60.00;
                    case "EP" -> 120.00;
                    case "US", "JP" -> 250.00;
                    default -> 300.00;
                };

                var titlesJson = entry.getValue().entrySet().stream()
                    .map(t -> "    \"%s\": \"%s\"".formatted(t.getKey(), t.getValue()))
                    .collect(Collectors.joining(",\n"));

                // 3. Text Blocks & formatted
                return """
                    {
                      "id": "%s",
                      "titel": {
%s
                      },
                      "gebuehr": %.2f
                    }""" .formatted(id, titlesJson, gebuehr);
            })
            .collect(Collectors.joining(",\n"));

        // 4. Ausgabe
        System.out.println("[" + jsonOutput + "]");
    }
}
```

```
