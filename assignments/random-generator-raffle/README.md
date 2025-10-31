# Random Generator Raffle – Ausgangspunkt für java.util.random

Dieses Modul simuliert eine Firmenverlosung mit reichlich „Legacy-Randomness“. In den Services arbeiten noch klassische `java.util.Random`-Instanzen mit globalem Zustand, fragwürdiger Verteilung und kaum reproduzierbaren Simulationen. Deine Aufgabe ist es, die neuen `java.util.random`-APIs aus Java 17 einzusetzen und robuste Zufallsströme aufzubauen.

## Lernziele
- Unterschiede zwischen `Random`, `ThreadLocalRandom` und der neuen `RandomGenerator`-Familie verstehen
- Geeignete Generatoren per `RandomGeneratorFactory` auswählen und mit Seeds reproduzierbar machen
- Splittable/Jumpable-Generatoren für Simulationen und parallele Streams anwenden

## Hands-on-Aufgaben
1. Ersetzt die `Random`-Felder in `LegacyRandomService` durch passende `RandomGenerator`-Varianten (z.B. `L64X128MixRandom`) und kapselt die Auswahl in einer Konfiguration.
2. Baut eine neue `RaffleRandomService`, die über `RandomGeneratorFactory` deterministische Seeds, unabhängige Splits und Statistik-Funktionen bereitstellt. Der Controller soll anschließend ausschließlich diesen Service verwenden.
3. Implementiert eine Simulation, die mithilfe von `RandomGenerator.SplittableGenerator` mehrere Teilströme erzeugt (z.B. für Regionen) und die Verteilungen im `/api/raffle/distribution`-Endpoint differenziert ausgibt.
4. Ergänzt Tests, die über identische Seeds deterministische Ergebnisse verifizieren (Ticketnummern, Gewinner, Verteilungen).

## Starten
```bash
mvn -pl assignments/random-generator-raffle spring-boot:run
```
Port: 9086
