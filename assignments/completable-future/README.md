# CompletableFuture – Hands-on

Dieses Modul übt die wichtigsten Operatoren von `java.util.concurrent.CompletableFuture`: asynchrone Starts, Transformieren, Komponieren, Kombinieren, Warten auf mehrere Futures, erstes Ergebnis ermitteln, Fehler abfedern sowie Timeouts.

## Lernziele
- `supplyAsync`, `thenApply`, `thenCompose`, `thenCombine` gezielt einsetzen
- Ergebnisse mehrerer Futures mit `allOf`/`anyOf` sammeln
- Fehlerbehandlung mit `exceptionally`/`handle` und Fallbacks
- Timeouts und Fallback-Werte konfigurieren (`completeOnTimeout`, `orTimeout`)

## Hands-on-Aufgaben
1. Öffne `src/main/java/tech/erben/java17/completablefuture/CompletableFutureWorkshop.java`.
2. Jede Methode wirft noch `UnsupportedOperationException`, die Javadoc erklärt, welche CompletableFuture-Operatoren verwendet werden sollen.
3. Arbeite Dich Test für Test durch `src/test/java/tech/erben/java17/completablefuture/CompletableFutureWorkshopTest.java`. Sobald alle Methoden korrekt implementiert sind, werden die Tests grün.

## Tests ausführen
```bash
mvn -pl assignments/completable-future test -DskipTests=false
```
