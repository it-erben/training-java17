import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.Executors;

/**
 * A simple local HTTP server that mocks postman-echo.com behavior.
 * Run this main method before running the Task assignments.
 * Pure JDK, no extra dependencies needed.
 */
public class LocalEchoServer {

    private static final int PORT = 8080;

    public static void main(String[] a) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        // Use a thread pool so multiple requests (like in Task 5 async) can be handled in parallel
        server.setExecutor(Executors.newCachedThreadPool());

        // Task 1, 5: /get
        server.createContext("/get", exchange -> {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                send(exchange, 405, "Method Not Allowed");
                return;
            }
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("url", "http://localhost:" + PORT + exchange.getRequestURI());
            Map<String, String> args = parseQuery(exchange.getRequestURI().getQuery());
            response.put("args", args);

            // Simple mock data for Task 5 scenarios
            String q = args.get("q");
            if ("status".equals(q)) response.put("result", "System Operational");
            if ("quota".equals(q)) response.put("result", "100/5000");
            if ("news".equals(q)) response.put("result", "No new notifications");

            sendJson(exchange, 200, response);
        });

        // Task 2: /status/404
        server.createContext("/status/", exchange -> {
            String path = exchange.getRequestURI().getPath();
            String codeStr = path.substring(path.lastIndexOf('/') + 1);
            try {
                int code = Integer.parseInt(codeStr);
                if (code == 200) {
                    sendJson(exchange, 200, Map.of("status", 200, "message", "Akte gefunden!"));
                } else if (code == 404) {
                     // Send 404 without body or small body
                     exchange.sendResponseHeaders(404, 0);
                     exchange.getResponseBody().close();
                } else {
                    sendJson(exchange, code, Map.of("status", code));
                }
            } catch (NumberFormatException e) {
                send(exchange, 400, "Bad Request");
            }
        });

        // Task 3: /post
        server.createContext("/post", exchange -> {
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                send(exchange, 405, "Only POST allowed");
                return;
            }
            // Read body
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("data", body); // Echo body
            // Headers echo - simplified
            Map<String, String> headers = new LinkedHashMap<>();
            exchange.getRequestHeaders().forEach((k, v) -> headers.put(k, String.join(",", v)));
            response.put("headers", headers);

            sendJson(exchange, 200, response);
        });

        // Task 4: /delay/5
        server.createContext("/delay/", exchange -> {
            String path = exchange.getRequestURI().getPath();
            String delayStr = path.substring(path.lastIndexOf('/') + 1);
            try {
                int delay = Integer.parseInt(delayStr);
                // Sleep to simulate delay
                Thread.sleep(delay * 1000L);
                sendJson(exchange, 200, Map.of("delay", delay));
            } catch (InterruptedException e) {
                send(exchange, 500, "Interrupted");
            } catch (NumberFormatException e) {
                send(exchange, 400, "Bad Delay format");
            }
        });

        // Task 6: /stream/5
        server.createContext("/stream/", exchange -> {
            String path = exchange.getRequestURI().getPath();
            String linesStr = path.substring(path.lastIndexOf('/') + 1);
            int lines = 5;
            try { lines = Integer.parseInt(linesStr); } catch (Exception e) {}

            exchange.getResponseHeaders().add("Content-Type", "text/plain");
            exchange.sendResponseHeaders(200, 0); // Chunked/Streaming
            try (OutputStream os = exchange.getResponseBody()) {
                for (int i = 0; i < lines; i++) {
                    String line = "Line " + (i + 1) + ": Data package " + System.nanoTime() + "\n";
                    os.write(line.getBytes(StandardCharsets.UTF_8));
                    os.flush();
                    try { Thread.sleep(200); } catch (InterruptedException e) {}
                }
            }
        });

        System.out.println("=============================================");
        System.out.println("Local Echo Server started on port " + PORT);
        System.out.println("Endpoints available:");
        System.out.println("  GET  http://localhost:" + PORT + "/get");
        System.out.println("  GET  http://localhost:" + PORT + "/status/{code}");
        System.out.println("  POST http://localhost:" + PORT + "/post");
        System.out.println("  GET  http://localhost:" + PORT + "/delay/{seconds}");
        System.out.println("  GET  http://localhost:" + PORT + "/stream/{lines}");
        System.out.println("=============================================");
        server.start();
    }

    private static void sendJson(HttpExchange exchange, int statusCode, Object data) throws IOException {
        String json = toJson(data);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        send(exchange, statusCode, json);
    }

    private static void send(HttpExchange exchange, int statusCode, String responseBody) throws IOException {
        byte[] bytes = responseBody.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static String toJson(Object value) {
        if (value instanceof Map<?, ?> map) {
            StringJoiner joiner = new StringJoiner(",", "{", "}");
            map.forEach((k, v) -> joiner.add("\"" + escape(String.valueOf(k)) + "\":" + toJson(v)));
            return joiner.toString();
        }
        if (value instanceof Number || value instanceof Boolean) {
            return String.valueOf(value);
        }
        if (value == null) {
            return "null";
        }
        return "\"" + escape(value.toString()) + "\"";
    }

    private static Map<String, String> parseQuery(String query) {
        Map<String, String> result = new LinkedHashMap<>();
        if (query == null || query.isEmpty()) {
            return result;
        }
        for (String param : query.split("&")) {
            if (param.isEmpty()) {
                continue;
            }
            String[] entry = param.split("=", 2);
            String key = urlDecode(entry[0]);
            String value = entry.length > 1 ? urlDecode(entry[1]) : "";
            result.put(key, value);
        }
        return result;
    }

    private static String urlDecode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    private static String escape(String input) {
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}
