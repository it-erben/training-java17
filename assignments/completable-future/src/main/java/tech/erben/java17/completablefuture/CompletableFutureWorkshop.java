package tech.erben.java17.completablefuture;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

/**
 * Skeleton für eine Reihe von CompletableFuture-Übungen.
 * Die Javadoc beschreibt, welche CF-Operatoren eingesetzt werden sollen; die Tests prüfen das Verhalten.
 */
public class CompletableFutureWorkshop {

    private final Executor executor;

    public CompletableFutureWorkshop() {
        this(CompletableFuture.delayedExecutor(0, java.util.concurrent.TimeUnit.MILLISECONDS));
    }

    public CompletableFutureWorkshop(Executor executor) {
        this.executor = executor;
    }

    /**
     * Starte eine asynchrone Berechnung mit {@link CompletableFuture#supplyAsync(Supplier, Executor)} auf dem bereitgestellten Executor.
     * Der Supplier soll erst im Executor laufen (nicht direkt im Aufrufer-Thread), das Ergebnis ist der zurückgegebene {@link CompletableFuture}.
     */
    public CompletableFuture<String> startAsync(Supplier<String> supplier) {
        throw new UnsupportedOperationException("Implementieren");
    }

    /**
     * Wandle das Ergebnis eines bestehenden Futures in Großbuchstaben um.
     * Nutze {@link CompletableFuture#thenApply(java.util.function.Function)} und gib den transformierten Future zurück.
     */
    public CompletableFuture<String> toUpperCase(CompletableFuture<String> input) {
        throw new UnsupportedOperationException("Implementieren");
    }

    /**
     * Hänge ein asynchron geladenes Suffix an eine vorhandene Zeichenkette an.
     * Nutze {@link CompletableFuture#thenCompose(java.util.function.Function)} und darin erneut {@link CompletableFuture#supplyAsync(Supplier, Executor)}
     * mit dem vorhandenen Executor, um das Suffix zu laden.
     */
    public CompletableFuture<String> appendSuffixAsync(CompletableFuture<String> base, Supplier<String> suffixSupplier) {
        throw new UnsupportedOperationException("Implementieren");
    }

    /**
     * Kombiniere Vor- und Nachname zweier Futures mit {@link CompletableFuture#thenCombine(CompletableFuture, java.util.function.BiFunction)}.
     * Ergebnis: "<Vorname> <Nachname>".
     */
    public CompletableFuture<String> combineNameParts(CompletableFuture<String> firstName, CompletableFuture<String> lastName) {
        throw new UnsupportedOperationException("Implementieren");
    }

    /**
     * Warte auf alle Futures mittels {@link CompletableFuture#allOf(CompletableFuture[])} und liefere deren Ergebnisse in derselben Reihenfolge wie die Eingabeliste.
     * Tipp: Nach allOf() die einzelnen Futures mit {@link CompletableFuture#join()} auslesen.
     */
    public CompletableFuture<List<String>> allResults(List<CompletableFuture<String>> futures) {
        throw new UnsupportedOperationException("Implementieren");
    }

    /**
     * Liefere das Ergebnis des zuerst fertig werdenden Futures aus der Liste mit {@link CompletableFuture#anyOf(CompletableFuture...)}.
     */
    public CompletableFuture<String> firstFinished(List<CompletableFuture<String>> futures) {
        throw new UnsupportedOperationException("Implementieren");
    }

    /**
     * Liefere das Ergebnis des primären Futures, ersetze aber im Fehlerfall (Exception) durch einen Fallback-Wert aus dem Supplier.
     * Nutze {@link CompletableFuture#exceptionally(java.util.function.Function)} oder {@link CompletableFuture#handle(java.util.function.BiFunction)}.
     */
    public CompletableFuture<String> withFallback(CompletableFuture<String> primary, Supplier<String> fallbackSupplier) {
        throw new UnsupportedOperationException("Implementieren");
    }

    /**
     * Ergänze einen Timeout für das gegebene Future: falls es innerhalb der Duration nicht fertig wird, soll {@code fallbackValue} geliefert werden.
     * Nutze {@link CompletableFuture#completeOnTimeout(Object, long, java.util.concurrent.TimeUnit)} oder {@link CompletableFuture#orTimeout(long, java.util.concurrent.TimeUnit)} plus anschließenden Fallback.
     */
    public CompletableFuture<String> withTimeout(CompletableFuture<String> future, Duration timeout, String fallbackValue) {
        throw new UnsupportedOperationException("Implementieren");
    }
}
