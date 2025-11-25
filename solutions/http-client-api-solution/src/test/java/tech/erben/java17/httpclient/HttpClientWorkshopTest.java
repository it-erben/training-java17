package tech.erben.java17.httpclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class HttpClientWorkshopTest {

    private MockWebServer server;
    private HttpClientWorkshop workshop;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws IOException {
        server = new MockWebServer();
        server.start();
        workshop = new HttpClientWorkshop();
    }

    @AfterEach
    void tearDown() throws IOException {
        server.shutdown();
    }

    private URI uri(String path) {
        return server.url(path).uri();
    }

    @Test
    void fetchPlainText_shouldSetAcceptHeaderAndReturnBody() throws Exception {
        server.enqueue(new MockResponse()
            .addHeader("Content-Type", "text/plain")
            .setBody("pong"));

        String result = workshop.fetchPlainText(uri("/status/ping"));

        assertEquals("pong", result);
        RecordedRequest request = server.takeRequest(1, TimeUnit.SECONDS);
        assertNotNull(request);
        assertEquals("GET", request.getMethod());
        assertEquals("/status/ping", request.getPath());
        assertEquals("text/plain", request.getHeader("Accept"));
    }

    @Test
    void fetchJsonDocument_readsJsonPayload() throws Exception {
        server.enqueue(new MockResponse()
            .addHeader("Content-Type", "application/json")
            .setBody("""
                {"service":"catalog","enabled":true}"""));

        Map<String, Object> result = workshop.fetchJsonDocument(uri("/feature-flags"));

        assertEquals("catalog", result.get("service"));
        assertEquals(Boolean.TRUE, result.get("enabled"));
        RecordedRequest request = server.takeRequest(1, TimeUnit.SECONDS);
        assertNotNull(request);
        assertEquals("GET", request.getMethod());
        assertEquals("/feature-flags", request.getPath());
        assertEquals("application/json", request.getHeader("Accept"));
    }

    @Test
    void submitJsonOrder_sendsOrderAsJson() throws Exception {
        server.enqueue(new MockResponse()
            .setResponseCode(201)
            .addHeader("Content-Type", "application/json")
            .setBody("""
                {"status":"created","id":"order-17"}"""));

        OrderRequest order = new OrderRequest("coffee-beans", 3, true);
        HttpResponse<String> response = workshop.submitJsonOrder(uri("/orders"), order);

        assertEquals(201, response.statusCode());
        assertEquals("created", objectMapper.readTree(response.body()).get("status").asText());

        RecordedRequest request = server.takeRequest(1, TimeUnit.SECONDS);
        assertNotNull(request);
        assertEquals("POST", request.getMethod());
        assertEquals("/orders", request.getPath());
        assertEquals("application/json", request.getHeader("Content-Type"));
        assertEquals("application/json", request.getHeader("Accept"));

        var bodyJson = objectMapper.readTree(request.getBody().readUtf8());
        assertEquals("coffee-beans", bodyJson.get("sku").asText());
        assertEquals(3, bodyJson.get("quantity").asInt());
        assertTrue(bodyJson.get("express").asBoolean());
    }

    @Test
    void submitForm_encodesFormData() throws Exception {
        server.enqueue(new MockResponse().setBody("ok"));

        Map<String, String> form = new LinkedHashMap<>();
        form.put("filter", "popular");
        form.put("limit", "5");

        HttpResponse<String> response = workshop.submitForm(uri("/search"), form);

        assertEquals(200, response.statusCode());
        assertEquals("ok", response.body());
        RecordedRequest request = server.takeRequest(1, TimeUnit.SECONDS);
        assertNotNull(request);
        assertEquals("POST", request.getMethod());
        assertEquals("/search", request.getPath());
        assertEquals("application/x-www-form-urlencoded", request.getHeader("Content-Type"));

        Set<String> params = new HashSet<>(Arrays.asList(request.getBody().readUtf8().split("&")));
        assertEquals(Set.of("filter=popular", "limit=5"), params);
    }

    @Test
    void downloadReport_writesContentToTargetFile() throws Exception {
        byte[] report = "line-1\nline-2\n".getBytes(StandardCharsets.UTF_8);
        server.enqueue(new MockResponse().setBody(new Buffer().write(report)));

        Path target = Files.createTempFile("report", ".txt");
        Path downloaded = workshop.downloadReport(uri("/reports/daily"), target);

        assertEquals(target, downloaded);
        assertArrayEquals(report, Files.readAllBytes(downloaded));

        RecordedRequest request = server.takeRequest(1, TimeUnit.SECONDS);
        assertNotNull(request);
        assertEquals("GET", request.getMethod());
        assertEquals("/reports/daily", request.getPath());
    }

    @Test
    void streamServerLogLines_collectsAllLines() throws Exception {
        server.enqueue(new MockResponse()
            .addHeader("Content-Type", "text/plain")
            .setBody("alpha\nbeta\ngamma\n"));

        List<String> lines = workshop.streamServerLogLines(uri("/logs/stream"));

        assertEquals(List.of("alpha", "beta", "gamma"), lines);
        RecordedRequest request = server.takeRequest(1, TimeUnit.SECONDS);
        assertNotNull(request);
        assertEquals("GET", request.getMethod());
        assertEquals("/logs/stream", request.getPath());
    }

    @Test
    void fetchFeatureFlagsAsync_usesSendAsyncAndParsesResponse() throws Exception {
        server.enqueue(new MockResponse()
            .addHeader("Content-Type", "application/json")
            .setBodyDelay(500, TimeUnit.MILLISECONDS)
            .setBody("""
                ["new-ui","beta-opt-in"]"""));

        long start = System.nanoTime();
        var future = workshop.fetchFeatureFlagsAsync(uri("/feature-flags"));
        long elapsedMillis = Duration.ofNanos(System.nanoTime() - start).toMillis();
        assertTrue(elapsedMillis < 250, "sendAsync sollte ohne IO-Blockade schnell zurückkommen");

        List<String> flags = future.get(2, TimeUnit.SECONDS);

        assertEquals(List.of("new-ui", "beta-opt-in"), flags);
        RecordedRequest request = server.takeRequest(1, TimeUnit.SECONDS);
        assertNotNull(request);
        assertEquals("GET", request.getMethod());
        assertEquals("/feature-flags", request.getPath());
    }

    @Test
    void deleteResource_sendsDeleteAndDiscardsBody() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(204));

        HttpResponse<Void> response = workshop.deleteResource(uri("/resources"), "42");

        assertEquals(204, response.statusCode());
        RecordedRequest request = server.takeRequest(1, TimeUnit.SECONDS);
        assertNotNull(request);
        assertEquals("DELETE", request.getMethod());
        assertEquals("/resources/42", request.getPath());
        assertEquals(0, request.getBodySize());
    }

    @Test
    void followRedirect_followsLocationHeader() throws Exception {
        server.enqueue(new MockResponse()
            .setResponseCode(302)
            .addHeader("Location", "/final"));
        server.enqueue(new MockResponse()
            .addHeader("Content-Type", "text/plain")
            .setBody("done"));

        String body = workshop.followRedirect(uri("/redirect"));

        assertEquals("done", body);
        assertEquals(2, server.getRequestCount());
        RecordedRequest first = server.takeRequest(1, TimeUnit.SECONDS);
        RecordedRequest second = server.takeRequest(1, TimeUnit.SECONDS);
        assertNotNull(first);
        assertNotNull(second);
        assertEquals("/redirect", first.getPath());
        assertEquals("/final", second.getPath());
    }

    @Test
    void uploadFile_putsBytesFromDisk() throws Exception {
        server.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody("uploaded"));

        Path file = Files.createTempFile("payload", ".txt");
        Files.writeString(file, "file-content", StandardCharsets.UTF_8);

        HttpResponse<String> response = workshop.uploadFile(uri("/files/upload"), file, "text/plain");

        assertEquals(200, response.statusCode());
        assertEquals("uploaded", response.body());

        RecordedRequest request = server.takeRequest(1, TimeUnit.SECONDS);
        assertNotNull(request);
        assertEquals("PUT", request.getMethod());
        assertEquals("/files/upload", request.getPath());
        assertEquals("text/plain", request.getHeader("Content-Type"));
        assertEquals("file-content", request.getBody().readUtf8());
    }

    @Test
    void authenticatedPing_setsBearerToken() throws Exception {
        server.enqueue(new MockResponse()
            .addHeader("Content-Type", "text/plain")
            .setBody("ok"));

        HttpResponse<Void> response = workshop.authenticatedPing(uri("/auth/ping"), "token-123");

        assertEquals(200, response.statusCode());
        RecordedRequest request = server.takeRequest(1, TimeUnit.SECONDS);
        assertNotNull(request);
        assertEquals("GET", request.getMethod());
        assertEquals("/auth/ping", request.getPath());
        assertEquals("text/plain", request.getHeader("Accept"));
        assertEquals("Bearer token-123", request.getHeader("Authorization"));
        assertEquals(0, request.getBodySize());
    }
}
