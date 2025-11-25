package tech.erben.java17.completablefuture;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class CompletableFutureWorkshopTest {

    private ExecutorService executor;
    private CompletableFutureWorkshop workshop;

    @BeforeEach
    void setUp() {
        executor = Executors.newSingleThreadExecutor(r -> new Thread(r, "cf-test"));
        workshop = new CompletableFutureWorkshop(executor);
    }

    @AfterEach
    void tearDown() {
        executor.shutdownNow();
    }

    @Test
    void startAsync_runsOnProvidedExecutor() throws Exception {
        AtomicBoolean ranOnExecutor = new AtomicBoolean(false);
        Supplier<String> supplier = () -> {
            ranOnExecutor.set(Thread.currentThread().getName().equals("cf-test"));
            return "hello";
        };

        String result = workshop.startAsync(supplier).get(1, TimeUnit.SECONDS);

        assertEquals("hello", result);
        assertTrue(ranOnExecutor.get(), "Supplier sollte auf dem bereitgestellten Executor laufen");
    }

    @Test
    void toUpperCase_transformsResult() throws Exception {
        var base = CompletableFuture.completedFuture("mixedCase");

        String result = workshop.toUpperCase(base).get(1, TimeUnit.SECONDS);

        assertEquals("MIXEDCASE", result);
    }

    @Test
    void appendSuffixAsync_composesAsyncSuffix() throws Exception {
        AtomicBoolean suffixOnExecutor = new AtomicBoolean(false);
        var base = CompletableFuture.completedFuture("Hello");
        Supplier<String> suffixSupplier = () -> {
            suffixOnExecutor.set(Thread.currentThread().getName().equals("cf-test"));
            return " World";
        };

        String result = workshop.appendSuffixAsync(base, suffixSupplier).get(1, TimeUnit.SECONDS);

        assertEquals("Hello World", result);
        assertTrue(suffixOnExecutor.get(), "Suffix-Supplier sollte asynchron auf dem Executor laufen");
    }

    @Test
    void combineNameParts_mergesWithSpace() throws Exception {
        var first = CompletableFuture.completedFuture("Ada");
        var last = CompletableFuture.completedFuture("Lovelace");

        String result = workshop.combineNameParts(first, last).get(1, TimeUnit.SECONDS);

        assertEquals("Ada Lovelace", result);
    }

    @Test
    void allResults_waitsForAllAndKeepsOrder() throws Exception {
        var first = CompletableFuture.supplyAsync(() -> "one", executor);
        var second = CompletableFuture.supplyAsync(() -> {
            sleep(100);
            return "two";
        }, executor);
        var third = CompletableFuture.supplyAsync(() -> "three", executor);

        List<String> result = workshop.allResults(List.of(first, second, third)).get(2, TimeUnit.SECONDS);

        assertEquals(List.of("one", "two", "three"), result);
    }

    @Test
    void firstFinished_returnsFirstCompleted() throws Exception {
        var slow = CompletableFuture.supplyAsync(() -> {
            sleep(250);
            return "slow";
        }, executor);
        var fast = CompletableFuture.supplyAsync(() -> "fast", executor);

        long start = System.nanoTime();
        String result = workshop.firstFinished(List.of(slow, fast)).get(2, TimeUnit.SECONDS);
        long durationMs = Duration.ofNanos(System.nanoTime() - start).toMillis();

        assertEquals("fast", result);
        assertTrue(durationMs < 200, "Sollte schnell liefern, sobald erstes Future fertig ist");
    }

    @Test
    void withFallback_returnsFallbackOnException() throws Exception {
        var failed = new CompletableFuture<String>();
        failed.completeExceptionally(new IllegalStateException("boom"));
        AtomicBoolean fallbackUsed = new AtomicBoolean(false);

        String result = workshop.withFallback(failed, () -> {
            fallbackUsed.set(true);
            return "fallback";
        }).get(1, TimeUnit.SECONDS);

        assertEquals("fallback", result);
        assertTrue(fallbackUsed.get());
    }

    @Test
    void withFallback_passesThroughOnSuccess() throws Exception {
        AtomicBoolean fallbackUsed = new AtomicBoolean(false);
        var ok = CompletableFuture.completedFuture("value");

        String result = workshop.withFallback(ok, () -> {
            fallbackUsed.set(true);
            return "fallback";
        }).get(1, TimeUnit.SECONDS);

        assertEquals("value", result);
        assertFalse(fallbackUsed.get());
    }

    @Test
    void withTimeout_completesWithFallbackAfterDuration() throws Exception {
        var neverFinishes = new CompletableFuture<String>();

        long start = System.nanoTime();
        String result = workshop.withTimeout(neverFinishes, Duration.ofMillis(150), "timeout")
            .get(1, TimeUnit.SECONDS);
        long elapsedMs = Duration.ofNanos(System.nanoTime() - start).toMillis();

        assertEquals("timeout", result);
        assertTrue(elapsedMs >= 140, "Timeout sollte etwa der angegebenen Dauer entsprechen");
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
