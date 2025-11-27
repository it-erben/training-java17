package tech.erben.gfu.httpclient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Aufgaben rund um den Java 11 {@link HttpClient}, angelehnt an das Worksheet
 * <code>04-http.md</code>. Implementiere die fünf Methoden so, dass die dazugehörigen
 * Tests mit einem Mock-Server (WireMock) erfolgreich durchlaufen.
 *
 * <ol>
 *     <li><b>GET</b>: Baue einen GET-Request auf <code>/get?status=active</code>,
 *         sende synchron und liefere den Antwort-Body als String zurück.</li>
 *     <li><b>POST</b>: Baue einen POST auf <code>/post</code> mit JSON-Body
 *         <code>{"id": "...", "owner": "..."}</code> und Headern
 *         <code>Content-Type: application/json</code> und <code>X-Auth-Token: secret123</code>.
 *         Gib den Antwort-Body als String zurück.</li>
 *     <li><b>Timeout</b>: Sende einen Request auf <code>/delay/{seconds}</code> und setze
 *         ein Request-Timeout (z. B. 2 Sekunden). Wenn der Server länger braucht, soll eine
 *         {@link java.net.http.HttpTimeoutException} fliegen.</li>
 *     <li><b>Async</b>: Starte drei parallele Requests auf
 *         <code>/get?q=status</code>, <code>/get?q=quota</code>, <code>/get?q=news</code>,
 *         warte mit {@link CompletableFuture#allOf(CompletableFuture[])} bis alle fertig sind
 *         und gib die drei Antwort-Bodies als {@link DashboardData} zurück.</li>
 *     <li><b>Streaming</b>: Rufe <code>/stream/{lines}</code> auf, nutze
 *         {@link java.net.http.HttpResponse.BodyHandlers#ofLines()}, filtere leere Zeilen heraus,
 *         wandle die anderen in Großbuchstaben und liefere sie als Liste zurück.</li>
 * </ol>
 *
 * Nutze ausschließlich den JDK-HttpClient (keine Spring-REST-Clients) und stelle sicher, dass
 * alle Requests gegen die im Konstruktor übergebene <code>baseUri</code> laufen.
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
        throw new UnsupportedOperationException("Noch nicht implementiert");
    }

    /**
     * Aufgabe 2: POST /post mit JSON-Body und Headern.
     */
    public String postOwnerData(String id, String owner) throws IOException, InterruptedException {
        throw new UnsupportedOperationException("Noch nicht implementiert");
    }

    /**
     * Aufgabe 3: Timeout mit GET /delay/{seconds}. Setze ein Request-Timeout; bei Überschreitung
     * soll eine {@link java.net.http.HttpTimeoutException} entstehen.
     */
    public HttpResponse<String> callWithTimeout(int delayInSeconds, Duration timeout) throws IOException, InterruptedException {
        throw new UnsupportedOperationException("Noch nicht implementiert");
    }

    /**
     * Aufgabe 4: Async-Requests auf /get?q=status, /get?q=quota, /get?q=news parallel starten,
     * dann die Ergebnisse zusammenführen.
     */
    public DashboardData fetchDashboardData() throws IOException, InterruptedException {
        throw new UnsupportedOperationException("Noch nicht implementiert");
    }

    /**
     * Aufgabe 5: Streaming-Endpoint /stream/{lines} lesen, leere Zeilen filtern,
     * den Rest groß schreiben und als Liste zurückliefern.
     */
    public List<String> streamLines(int lines) throws IOException, InterruptedException {
        throw new UnsupportedOperationException("Noch nicht implementiert");
    }

    /**
     * Ergebnisobjekt für Aufgabe 4.
     */
    public record DashboardData(String status, String quota, String news) { }
}
