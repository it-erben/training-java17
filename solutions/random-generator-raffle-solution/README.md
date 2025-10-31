# Random Generator Raffle – Lösung

Die Referenzlösung setzt die neuen APIs aus `java.util.random` konsequent ein und räumt mit den alten `Random`-Instanzen auf. Sämtliche Zufallsquellen sind deterministisch reproduzierbar, splittbar für regionale Simulationen und liefern stabile Streams für Tickets sowie IDs.

## Highlights
- `RandomGeneratorFactory` wählt `L64X128MixRandom` aus und injiziert Seeds pro Kontext (`winner`, `tickets`, `distribution`).
- Stream-basierte Ticketnummern (`rng.ints`) und ID-Generierung (`rng.longs`) ohne manuelle Modulo-Spielchen.
- Regionale Verteilungssimulation über `RandomGenerator.SplittableGenerator.splits(...)` mit separaten Zufallsströmen.
- Endpoint `/api/raffle/generators` zeigt alle verfügbaren LXM-Generatoren auf.

## Ausprobieren
```bash
mvn -pl solutions/random-generator-raffle-solution spring-boot:run
```
Port: 9186
