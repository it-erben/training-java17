# Barbershop-Buchung mit Text Blocks

## Lernziele
- Text Blocks mit klassischen String-Literalen vergleichen und Gleichheit prüfen
- `stripIndent()` und `indent()` zur Steuerung von Einrückungen einsetzen
- `translateEscapes()` verwenden, um Escape-Sequenzen nachträglich auszuwerten
- `.formatted(...)` für HTML- und Tabellenvorlagen nutzen
- JSON- und SQL-Beispiele als Text Blocks pflegen

## Hands-on-Aufgaben
1. Ergänze `buildShowcase` um ein Beispiel für `String#translateEscapes`, das auch `\s` (Whitespace) demonstriert.
2. Füge einen weiteren Abschnitt im UI hinzu, der `String#indent` mit negativen Werten zeigt und erläutert.
3. Erzeuge ein zusätzliches Beispiel, das `formatted` mit numerischer Formatierung (`%,.2f`) nutzt und im Template anzeigt.
4. Schreibe Tests, die sicherstellen, dass `stripIndent()` und `indent()` die erwarteten Leerzeichen im SQL-Beispiel produzieren.

## Starten
```bash
mvn -pl assignments/text-blocks-barbershop spring-boot:run
```
Port: 9082
