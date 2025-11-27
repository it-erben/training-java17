# Streams – Lösung
Ausimplementierte `MovieRatingsService`-Variante inklusive Bonus-Sortierfunktion und Beispielen über das Film-Rating-Dataset.

## Highlights
- Vollständige Stream-Pipelines für Filtern, Aggregieren und Sortieren (Votes, Laufzeit, Genres, Durchschnittswerte).
- Bonus: generische `getRatingsSortedByField`-Überladung mit Null-Schutz, optional auch für bereits erzeugte Streams.
- `Main` demonstriert typische Auswertungen (Top-Drama-Filme, kurze Watchlist, Genre-Übersicht).

## Ausprobieren
```bash
# Tests durchlaufen
mvn -pl solutions/streams-solution test

# Kleine Demo (liest das CSV aus resources)
mvn -pl solutions/streams-solution -DskipTests exec:java -Dexec.mainClass=tech.erben.java17.streams.movies.Main
```
