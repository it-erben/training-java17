package tech.erben.java17.httpclient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public String fetchPlainText(URI endpoint) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(endpoint)
            .GET()
            .header("Accept", "text/plain")
            .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    public Map<String, Object> fetchJsonDocument(URI endpoint) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(endpoint)
            .GET()
            .header("Accept", "application/json")
            .build();
        String json = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        return objectMapper.readValue(json, Map.class);
    }

    public HttpResponse<String> submitJsonOrder(URI endpoint, OrderRequest order) throws IOException, InterruptedException {
        String body = objectMapper.writeValueAsString(order);
        HttpRequest request = HttpRequest.newBuilder(endpoint)
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> submitForm(URI endpoint, Map<String, String> formData) throws IOException, InterruptedException {
        String body = formData.entrySet()
            .stream()
            .map(e -> e.getKey() + "=" + e.getValue())
            .collect(Collectors.joining("&"));

        HttpRequest request = HttpRequest.newBuilder(endpoint)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public Path downloadReport(URI endpoint, Path targetFile) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(endpoint)
            .GET()
            .build();
        return client.send(request, HttpResponse.BodyHandlers.ofFile(targetFile)).body();
    }

    public List<String> streamServerLogLines(URI endpoint) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(endpoint)
            .GET()
            .header("Accept", "text/plain")
            .build();
        HttpResponse<Stream<String>> response = client.send(request, HttpResponse.BodyHandlers.ofLines());
        try (Stream<String> lines = response.body()) {
            return lines.toList();
        }
    }

    public CompletableFuture<List<String>> fetchFeatureFlagsAsync(URI endpoint) {
        HttpRequest request = HttpRequest.newBuilder(endpoint)
            .GET()
            .header("Accept", "application/json")
            .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenApply(body -> {
                try {
                    return objectMapper.readValue(body, new TypeReference<List<String>>() {
                    });
                } catch (IOException e) {
                    throw new CompletionException(e);
                }
            });
    }

    public HttpResponse<Void> deleteResource(URI endpoint, String resourceId) throws IOException, InterruptedException {
        String base = endpoint.toString();
        if (!base.endsWith("/")) {
            base += "/";
        }
        URI deleteUri = URI.create(base + resourceId);

        HttpRequest request = HttpRequest.newBuilder(deleteUri)
            .DELETE()
            .build();
        return client.send(request, HttpResponse.BodyHandlers.discarding());
    }

    public String followRedirect(URI endpoint) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(endpoint)
            .GET()
            .header("Accept", "text/plain")
            .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    public HttpResponse<String> uploadFile(URI endpoint, Path file, String contentType) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(endpoint)
            .header("Content-Type", contentType)
            .PUT(HttpRequest.BodyPublishers.ofFile(file))
            .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<Void> authenticatedPing(URI endpoint, String bearerToken) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(endpoint)
            .GET()
            .header("Authorization", "Bearer " + bearerToken)
            .header("Accept", "text/plain")
            .build();
        return client.send(request, HttpResponse.BodyHandlers.discarding());
    }
}
