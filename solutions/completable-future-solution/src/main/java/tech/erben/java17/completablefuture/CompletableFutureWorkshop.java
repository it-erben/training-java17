package tech.erben.java17.completablefuture;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

/**
 * Ausgefüllte Referenzlösung für die CompletableFuture-Übungen.
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
        return CompletableFuture.supplyAsync(supplier, executor);
    }

    /**
     * Wandle das Ergebnis eines bestehenden Futures in Großbuchstaben um.
     * Nutze {@link CompletableFuture#thenApply(java.util.function.Function)} und gib den transformierten Future zurück.
     */
    public CompletableFuture<String> toUpperCase(CompletableFuture<String> input) {
        return input.thenApply(String::toUpperCase);
    }

    /**
     * Hänge ein asynchron geladenes Suffix an eine vorhandene Zeichenkette an.
     * Nutze {@link CompletableFuture#thenCompose(java.util.function.Function)} und darin erneut {@link CompletableFuture#supplyAsync(Supplier, Executor)}
     * mit dem vorhandenen Executor, um das Suffix zu laden.
     */
    public CompletableFuture<String> appendSuffixAsync(CompletableFuture<String> base, Supplier<String> suffixSupplier) {
        return base.thenCompose(prefix ->
            CompletableFuture.supplyAsync(suffixSupplier, executor)
                .thenApply(suffix -> prefix + suffix)
        );
    }

    /**
     * Kombiniere Vor- und Nachname zweier Futures mit {@link CompletableFuture#thenCombine(CompletableFuture, java.util.function.BiFunction)}.
     * Ergebnis: "<Vorname> <Nachname>".
     */
    public CompletableFuture<String> combineNameParts(CompletableFuture<String> firstName, CompletableFuture<String> lastName) {
        return firstName.thenCombine(lastName, (first, last) -> first + " " + last);
    }

    /**
     * Warte auf alle Futures mittels {@link CompletableFuture#allOf(CompletableFuture[])} und liefere deren Ergebnisse in derselben Reihenfolge wie die Eingabeliste.
     * Tipp: Nach allOf() die einzelnen Futures mit {@link CompletableFuture#join()} auslesen.
     */
    public CompletableFuture<List<String>> allResults(List<CompletableFuture<String>> futures) {
        CompletableFuture<?>[] array = futures.toArray(CompletableFuture[]::new);
        return CompletableFuture.allOf(array)
            .thenApply(ignored -> futures.stream().map(CompletableFuture::join).toList());
    }

    /**
     * Liefere das Ergebnis des zuerst fertig werdenden Futures aus der Liste mit {@link CompletableFuture#anyOf(CompletableFuture...)}.
     */
    public CompletableFuture<String> firstFinished(List<CompletableFuture<String>> futures) {
        CompletableFuture<?>[] array = futures.toArray(CompletableFuture[]::new);
        return CompletableFuture.anyOf(array)
            .thenApply(String.class::cast);
    }

    /**
     * Liefere das Ergebnis des primären Futures, ersetze aber im Fehlerfall (Exception) durch einen Fallback-Wert aus dem Supplier.
     * Nutze {@link CompletableFuture#exceptionally(java.util.function.Function)} oder {@link CompletableFuture#handle(java.util.function.BiFunction)}.
     */
    public CompletableFuture<String> withFallback(CompletableFuture<String> primary, Supplier<String> fallbackSupplier) {
        return primary.exceptionally(ex -> fallbackSupplier.get());
    }

    /**
     * Ergänze einen Timeout für das gegebene Future: falls es innerhalb der Duration nicht fertig wird, soll {@code fallbackValue} geliefert werden.
     * Nutze {@link CompletableFuture#completeOnTimeout(Object, long, java.util.concurrent.TimeUnit)} oder {@link CompletableFuture#orTimeout(long, java.util.concurrent.TimeUnit)} plus anschließenden Fallback.
     */
    public CompletableFuture<String> withTimeout(CompletableFuture<String> future, Duration timeout, String fallbackValue) {
        return future.completeOnTimeout(fallbackValue, timeout.toMillis(), java.util.concurrent.TimeUnit.MILLISECONDS);
    }
}
