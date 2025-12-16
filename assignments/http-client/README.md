# Aufgabe: Java 11 HttpClient

Dieses Modul spiegelt die Übung aus `worksheets/04-http.md` wider, aber in einer Maven-Struktur mit JUnit-Tests und einem Mock-Server (WireMock).

## Was zu tun ist
- Entferne die `@Disabled`-Annotation aus der `HttpClientAssignmentsTest`-Klasse, um die Tests zu aktivieren.
- Implementiere alle Methoden in `tech/erben/gfu/httpclient/HttpClientAssignments.java` exakt so, wie es die JavaDoc beschreibt.
- Nutze ausschließlich den JDK-`HttpClient` (kein RestTemplate/WebClient).
- Die Tests simulieren den Local-Echo-Server aus dem Worksheet: `HttpClientAssignmentsTest` startet dafür automatisch WireMock.

## Endpunkte (Mock)
- `GET /get?status=active` → liefere Body als String zurück.
- `POST /post` mit JSON `{"id":"...","owner":"..."}` und Headern `Content-Type: application/json`, `X-Auth-Token: secret123`.
- `GET /delay/{seconds}` mit Request-Timeout: bei Überschreitung muss eine `HttpTimeoutException` fliegen.
- `GET /get?q=status|quota|news` werden parallel abgefragt und als `DashboardData` zurückgegeben.
- `GET /stream/{lines}` → `BodyHandlers.ofLines()`, leere Zeilen filtern, Rest uppercase.

## Ausführen
```bash
mvn -pl assignments/http-client test
```
