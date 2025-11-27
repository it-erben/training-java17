package tech.erben.gfu.httpclient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * Implementierte Lösung der Aufgaben aus dem Modul {@code assignments/http-client}.
 */
public class HttpClientAssignments {

    private final HttpClient client;
    private final URI baseUri;

    public HttpClientAssignments(HttpClient client, URI baseUri) {
        this.client = client;
        this.baseUri = baseUri;
    }

    /**
     * Helper zum schnellen Instanziieren mit Standard-Client.
     */
    public static HttpClientAssignments forBaseUrl(String baseUrl) {
        return new HttpClientAssignments(HttpClient.newHttpClient(), URI.create(baseUrl));
    }

    /**
     * Aufgabe 1: GET /get?status=active
     */
    public String fetchActiveStatus() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(
                        baseUri.resolve("/get?status=active"))
                .GET()
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString())
                .body();
    }

    /**
     * Aufgabe 2: POST /post mit JSON-Body und Headern.
     */
    public String postOwnerData(String id, String owner) throws IOException, InterruptedException {
        String json = """
                {
                  "id": "%s",
                  "owner": "%s"
                }
                """.formatted(id, owner);

        HttpRequest request = HttpRequest.newBuilder(
                        baseUri.resolve("/post"))
                .header("Content-Type", "application/json")
                .header("X-Auth-Token", "secret123")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString())
                .body();
    }

    /**
     * Aufgabe 3: Timeout mit GET /delay/{seconds}. Setze ein Request-Timeout; bei Überschreitung
     * soll eine {@link java.net.http.HttpTimeoutException} entstehen.
     */
    public HttpResponse<String> callWithTimeout(int delayInSeconds, Duration timeout) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(
                        baseUri.resolve("/delay/" + delayInSeconds))
                .timeout(timeout)
                .GET()
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Aufgabe 4: Async-Requests auf /get?q=status, /get?q=quota, /get?q=news parallel starten,
     * dann die Ergebnisse zusammenführen.
     */
    public DashboardData fetchDashboardData() {
        CompletableFuture<String> status = sendAsyncWithQuery("status");
        CompletableFuture<String> quota = sendAsyncWithQuery("quota");
        CompletableFuture<String> news = sendAsyncWithQuery("news");

        CompletableFuture.allOf(status, quota, news).join();

        return new DashboardData(status.join(), quota.join(), news.join());
    }

    private CompletableFuture<String> sendAsyncWithQuery(String query) {
        HttpRequest request = HttpRequest.newBuilder(
                        baseUri.resolve("/get?q=" + query))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }

    /**
     * Aufgabe 5: Streaming-Endpoint /stream/{lines} lesen, leere Zeilen filtern,
     * den Rest groß schreiben und als Liste zurückliefern.
     */
    public List<String> streamLines(int lines) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(
                        baseUri.resolve("/stream/" + lines))
                .GET()
                .build();

        HttpResponse<Stream<String>> response = client.send(request, HttpResponse.BodyHandlers.ofLines());
        try (Stream<String> body = response.body()) {
            return body.filter(line -> !line.isBlank())
                    .map(String::toUpperCase)
                    .toList();
        }
    }

    /**
     * Ergebnisobjekt für Aufgabe 4.
     */
    public record DashboardData(String status, String quota, String news) { }
}
