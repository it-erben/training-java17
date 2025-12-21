# HTTP Client Lösung

Fertig implementierte Variante des Moduls `assignments/http-client`. Die Klasse `HttpClientAssignments` demonstriert:

- synchrone GET-/POST-Aufrufe mit dem JDK-HttpClient
- Request-Timeouts
- parallele Requests mit `CompletableFuture`
- Streaming-Antworten per `BodyHandlers.ofLines()`

Tests startest du mit:

```bash
mvn -pl solutions/http-client-solution test
```
