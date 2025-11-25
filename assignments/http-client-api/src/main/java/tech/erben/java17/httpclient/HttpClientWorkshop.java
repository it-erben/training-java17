package tech.erben.java17.httpclient;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Skeleton service that should be implemented with the java.net.http.HttpClient API.
 * The accompanying tests describe the intended usage for synchronous and asynchronous calls.
 */
public class HttpClientWorkshop {

    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public HttpClientWorkshop() {
        this(HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build(), new ObjectMapper());
    }

    public HttpClientWorkshop(HttpClient client, ObjectMapper objectMapper) {
        this.client = client;
        this.objectMapper = objectMapper;
    }

    /**
     * Schickt einen GET-Request auf das Endpoint-URI, setzt {@code Accept: text/plain}
     * und gibt den Text-Body als String zurück.
     * Nutze {@link java.net.http.HttpRequest.BodyPublishers#noBody()} sowie
     * {@link java.net.http.HttpResponse.BodyHandlers#ofString()}.
     */
    public String fetchPlainText(URI endpoint) throws IOException, InterruptedException {
        throw new UnsupportedOperationException("Implement using HttpClient");
    }

    /**
     * Schickt einen GET-Request mit {@code Accept: application/json} und parst den JSON-Body in eine {@code Map}.
     * Vorgehen: Body als String laden ({@link java.net.http.HttpResponse.BodyHandlers#ofString()})
     * und mit dem bereitgestellten {@link ObjectMapper} deserialisieren, z.B. {@code objectMapper.readValue(body, Map.class)}.
     */
    public Map<String, Object> fetchJsonDocument(URI endpoint) throws IOException, InterruptedException {
        throw new UnsupportedOperationException("Implement using HttpClient");
    }

    /**
     * POSTet die übergebene Order als JSON an das Endpoint-URI
     * ({@code Content-Type: application/json}, {@code Accept: application/json}).
     * Serialisiere {@link OrderRequest} mit dem vorhandenen {@link ObjectMapper}, z.B. {@code objectMapper.writeValueAsString(order)},
     * und gib die gesamte String-Response zurück.
     */
    public HttpResponse<String> submitJsonOrder(URI endpoint, OrderRequest order) throws IOException, InterruptedException {
        throw new UnsupportedOperationException("Implement using HttpClient");
    }

    /**
     * POSTet Form-Daten als {@code application/x-www-form-urlencoded}, gebaut aus der Map.
     * Forme Paare zu {@code key=value} und trenne mit {@code &}; gib die String-Response zurück.
     */
    public HttpResponse<String> submitForm(URI endpoint, Map<String, String> formData) throws IOException, InterruptedException {
        throw new UnsupportedOperationException("Implement using HttpClient");
    }

    /**
     * Lädt den Body eines GET-Requests in die angegebene Zieldatei.
     * Nutze {@link java.net.http.HttpResponse.BodyHandlers#ofFile(Path)} und gib den {@link Path} zurück.
     */
    public Path downloadReport(URI endpoint, Path targetFile) throws IOException, InterruptedException {
        throw new UnsupportedOperationException("Implement using HttpClient");
    }

    /**
     * Führt einen GET auf einen Textstream aus und sammelt alle Zeilen per {@link java.net.http.HttpResponse.BodyHandlers#ofLines()}.
     * Schließe den Stream und gib alle Zeilen in Reihenfolge als Liste zurück.
     */
    public List<String> streamServerLogLines(URI endpoint) throws IOException, InterruptedException {
        throw new UnsupportedOperationException("Implement using HttpClient");
    }

    /**
     * Führt asynchron einen GET mit {@code Accept: application/json} per {@link HttpClient#sendAsync} aus,
     * erwartet ein JSON-Array von Strings und parst es mit dem vorhandenen {@link ObjectMapper} in eine {@code List<String>}.
     * Tipp: {@code objectMapper.readValue(body, new com.fasterxml.jackson.core.type.TypeReference<List<String>>() { })}.
     * Gib das {@link CompletableFuture} direkt zurück (nicht blockieren).
     */
    public CompletableFuture<List<String>> fetchFeatureFlagsAsync(URI endpoint) {
        throw new UnsupportedOperationException("Implement using HttpClient");
    }

    /**
     * DELETE auf die Ressource mit {@code resourceId} (Endpoint um {@code /<id>} erweitern) ohne Request-Body.
     * Nutze {@link java.net.http.HttpResponse.BodyHandlers#discarding()} für die Antwort.
     */
    public HttpResponse<Void> deleteResource(URI endpoint, String resourceId) throws IOException, InterruptedException {
        throw new UnsupportedOperationException("Implement using HttpClient");
    }

    /**
     * Führt einen GET aus, folgt Redirects (Client ist mit {@link HttpClient.Redirect#NORMAL} gebaut)
     * und liefert den finalen Text-Body.
     * Verwende {@code Accept: text/plain} und {@link java.net.http.HttpResponse.BodyHandlers#ofString()}.
     */
    public String followRedirect(URI endpoint) throws IOException, InterruptedException {
        throw new UnsupportedOperationException("Implement using HttpClient");
    }

    /**
     * PUTtet den Inhalt der Datei zum Endpoint mit dem gegebenen {@code Content-Type}.
     * Nutze {@link java.net.http.HttpRequest.BodyPublishers#ofFile(Path)} und {@link java.net.http.HttpResponse.BodyHandlers#ofString()}.
     */
    public HttpResponse<String> uploadFile(URI endpoint, Path file, String contentType) throws IOException, InterruptedException {
        throw new UnsupportedOperationException("Implement using HttpClient");
    }

    /**
     * Schickt einen authentifizierten GET mit {@code Authorization: Bearer <token>} und {@code Accept: text/plain}.
     * Nutze {@link java.net.http.HttpRequest.BodyPublishers#noBody()} und {@link java.net.http.HttpResponse.BodyHandlers#discarding()} für eine leere Antwort.
     */
    public HttpResponse<Void> authenticatedPing(URI endpoint, String bearerToken) throws IOException, InterruptedException {
        throw new UnsupportedOperationException("Implement using HttpClient");
    }
}
