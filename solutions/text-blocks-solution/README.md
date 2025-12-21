# Text Blocks – Lösung

Vorbereitete Text-Block-Varianten der Templates aus `assignments/text-blocks`.

## Highlights

- HTML-Welcome- und Reminder-Mails als Text Blocks mit `formatted`/`stripIndent()` und dynamischer Startzeit.
- SQL-Queries mit lesbarer Einrückung, placeholder-Join für `IN (...)` und eingebautem Kommentar mit Quotes/Backslashes.
- Markdown-Renderer mit Bullet-Liste, eingebetteter Tabelle und Text-Block-Beispielpayload.
- Playground zeigt die Ergebnisse mit einfachen ASCII-Divider-Blocks.

## Ausprobieren

```bash
mvn -pl solutions/text-blocks-solution -DskipTests exec:java \
  -Dexec.mainClass=tech.erben.gfu.textblocks.TextBlockPlayground
```
