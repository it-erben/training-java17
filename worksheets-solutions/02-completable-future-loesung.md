# Musterlösung: CompletableFuture

## SmartHomeAsync.java

```java
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.*;

public class SmartHomeAsync {

    public static void main(String[] args) {
        System.out.println("--- LEICHT ---");
        aufgabe1bis3_Pipeline();
        aufgabe4_Fehler();

        System.out.println("\n--- MITTEL ---");
        aufgabe5_Compose();
        aufgabe6_Combine();
        aufgabe7_AllOf();

        System.out.println("\n--- SCHWER ---");
        aufgabe8_Timeouts();
        aufgabe9_Executor();
    }

    // --- LEICHT ---

    private static void aufgabe1bis3_Pipeline() {
        CompletableFuture.supplyAsync(() -> {
            sleep(1000); // Simulation Messung
            return 25.5;
        })
        .thenApply(grad -> grad + " °C") // Aufgabe 2: Transformation
        .thenApply(text -> "WERT: " + text)
        .thenAccept(System.out::println) // Aufgabe 3: Konsumieren
        .join(); // Warten, damit main nicht endet
    }

    private static void aufgabe4_Fehler() {
        CompletableFuture.supplyAsync(() -> {
            throw new RuntimeException("Sensorausfall");
        })
        .thenApply(grad -> grad + " °C")
        .thenApply(text -> "WERT: " + text)
        .exceptionally(ex -> {
            System.out.println("Fehler abgefangen: " + ex.getMessage());
            return "Keine Daten (Backup)"; // Fallback-Wert laut Aufgabe
        })
        .thenAccept(System.out::println)
        .join();
    }

    // --- MITTEL ---

    private static void aufgabe5_Compose() {
        // Schritt A liefert Ergebnis für Schritt B (Dateien auf dem Desktop)
        CompletableFuture.supplyAsync(SmartHomeAsync::loadCamIdFromFile)
            .thenCompose(camId -> CompletableFuture.supplyAsync(() -> loadImageBytesForCam(camId)))
            .thenAccept(bytes -> System.out.println("Geladenes Bild (" + bytes.length + " Bytes)"))
            .join();
    }

    private static void aufgabe6_Combine() {
        // Liest beide Sensorwerte vom Desktop und kombiniert sie
        var sensorWohnzimmer = CompletableFuture.supplyAsync(() -> loadTemperature("temp-wohnzimmer.txt"));
        var sensorKueche = CompletableFuture.supplyAsync(() -> loadTemperature("temp-kueche.txt"));

        // Beide müssen fertig sein
        sensorWohnzimmer.thenCombine(sensorKueche, (temp1, temp2) -> (temp1 + temp2) / 2)
            .thenAccept(avg -> System.out.println("Durchschnittstemperatur: " + avg))
            .join();
    }

    private static void aufgabe7_AllOf() {
        var licht1 = CompletableFuture.supplyAsync(() -> {
            sleep(100);
            return "Wohnzimmer: Aus";
        });
        var licht2 = CompletableFuture.supplyAsync(() -> {
            sleep(200);
            return "Küche: Aus";
        });
        var licht3 = CompletableFuture.supplyAsync(() -> {
            sleep(150);
            return "Bad: Aus";
        });

        CompletableFuture.allOf(licht1, licht2, licht3)
            .thenRun(() -> {
                System.out.println(licht1.join());
                System.out.println(licht2.join());
                System.out.println(licht3.join());
                System.out.println("Alles dunkel (Alle Lichter aus)");
            })
            .join();
    }

    // --- SCHWER ---

    private static void aufgabe8_Timeouts() {
        // Variante mit TimeoutException
        CompletableFuture.supplyAsync(() -> {
            sleep(5000); // Braucht zu lange
            return "Wetterdaten";
        })
        .orTimeout(1, TimeUnit.SECONDS)
        .exceptionally(ex -> "Wetterdienst nicht erreichbar")
        .thenAccept(System.out::println)
        .join();
        
        // Alternative: Default-Wert auf Timeout setzen
        CompletableFuture.supplyAsync(() -> {
            sleep(5000);
            return "Wetterdaten";
        })
        .completeOnTimeout("Standard-Wetter (Timeout)", 1, TimeUnit.SECONDS)
        .thenAccept(System.out::println)
        .join();
    }

    private static void aufgabe9_Executor() {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        
        CompletableFuture.supplyAsync(() -> {
            return "Task auf Custom Executor: " + Thread.currentThread().getName();
        }, executor)
        .thenAccept(System.out::println)
        .join();

        executor.shutdown();
    }

    private static Path desktopPath() {
        return Path.of(System.getProperty("user.home"), "Desktop");
    }

    private static String loadCamIdFromFile() {
        Path camIdFile = desktopPath().resolve("camera-id.txt");
        try {
            return Files.readString(camIdFile).trim();
        } catch (IOException e) {
            throw new UncheckedIOException("Konnte Kamera-ID nicht lesen: " + camIdFile, e);
        }
    }

    private static byte[] loadImageBytesForCam(String camId) {
        Path imageFile = desktopPath().resolve(camId + ".jpg"); // oder .png
        try {
            return Files.readAllBytes(imageFile);
        } catch (IOException e) {
            throw new UncheckedIOException("Konnte Bild nicht lesen: " + imageFile, e);
        }
    }

    private static double loadTemperature(String filename) {
        Path file = desktopPath().resolve(filename);
        try {
            return Double.parseDouble(Files.readString(file).trim());
        } catch (IOException e) {
            throw new UncheckedIOException("Konnte Temperatur nicht lesen: " + file, e);
        }
    }

    // Hilfsmethode
    private static void sleep(long millis) {
        try { Thread.sleep(millis); } catch (InterruptedException e) { } // Added missing closing brace and semicolon
    }
}
```
