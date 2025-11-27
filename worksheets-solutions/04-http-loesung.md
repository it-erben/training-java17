# Musterlösung: HTTP Client

```java
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class HttpClientSolution {
    
    private static final HttpClient client = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    public static void main(String[] args) throws Exception {
        
        // Aufgabe 1: Health-Check (Synchron)
        var request = HttpRequest.newBuilder()
                .uri(URI.create("https://postman-echo.com/get?status=active"))
                .GET()
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Status: " + response.statusCode());
        System.out.println("Body: " + response.body());

        // Aufgabe 2: 404 Handling
        var req404 = HttpRequest.newBuilder(URI.create("https://postman-echo.com/status/404")).build();
        var res404 = client.send(req404, HttpResponse.BodyHandlers.ofString());
        if (res404.statusCode() == 200) {
            System.out.println("Akte gefunden! Body: " + res404.body());
        } else if (res404.statusCode() == 404) {
            System.out.println("Fehler: Aktenzeichen existiert nicht.");
        } else {
            System.out.println("Unerwarteter Fehler: " + res404.statusCode());
        }

        // Aufgabe 3: POST mit JSON
        var json = """
                {"id": "DE-999", "owner": "Mustermann"}""";
        var postReq = HttpRequest.newBuilder()
                .uri(URI.create("https://postman-echo.com/post"))
                .header("Content-Type", "application/json")
                .header("X-Auth-Token", "secret123")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        var postRes = client.send(postReq, HttpResponse.BodyHandlers.discarding());
        if (postRes.statusCode() == 200) {
            System.out.println("POST erfolgreich (200).");
        } else {
            System.out.println("POST fehlgeschlagen, Status: " + postRes.statusCode());
        }

        // Aufgabe 4: Timeouts
        try {
            var timeoutReq = HttpRequest.newBuilder(URI.create("https://postman-echo.com/delay/5"))
                    .timeout(Duration.ofSeconds(2))
                    .build();
            client.send(timeoutReq, HttpResponse.BodyHandlers.ofString());
        } catch (HttpTimeoutException | IOException e) {
            System.out.println("Abbruch: Das Archiv antwortet nicht rechtzeitig.");
        }

        // Aufgabe 5: Async
        var f1 = client.sendAsync(HttpRequest.newBuilder(URI.create("https://postman-echo.com/get?q=status")).build(), HttpResponse.BodyHandlers.ofString());
        var f2 = client.sendAsync(HttpRequest.newBuilder(URI.create("https://postman-echo.com/get?q=quota")).build(), HttpResponse.BodyHandlers.ofString());
        var f3 = client.sendAsync(HttpRequest.newBuilder(URI.create("https://postman-echo.com/get?q=news")).build(), HttpResponse.BodyHandlers.ofString());

        CompletableFuture.allOf(f1, f2, f3).join();
        System.out.println("Alle Daten geladen!");
        System.out.println("Status: " + f1.get().body());
        System.out.println("Quota: " + f2.get().body());
        System.out.println("News: " + f3.get().body());

        // Aufgabe 6: Streaming (Lines)
        var streamReq = HttpRequest.newBuilder(URI.create("https://postman-echo.com/stream/5")).build();
        client.sendAsync(streamReq, HttpResponse.BodyHandlers.ofLines())
                .thenApply(HttpResponse::body)
                .exceptionally(ex -> Stream.of("Daten nicht verfügbar"))
                .thenAccept(lines -> lines
                        .filter(line -> !line.isBlank())
                        .map(String::toUpperCase)
                        .forEach(System.out::println))
                .join();
    }
}
```
