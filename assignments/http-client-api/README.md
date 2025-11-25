# Java HttpClient – Hands-on

Dieses Modul übt die `java.net.http.HttpClient`-API anhand eines kleinen Service-Skeletts. Die enthaltenen Tests beschreiben typische Einsatzszenarien, die Du mit dem HttpClient beherrschen solltest.

## Lernziele
- Synchrone Requests bauen (GET/POST/PUT/DELETE) inkl. Headern, Query-Params und unterschiedlichen Body Publishern
- JSON übermitteln und parsen (ObjectMapper), Form-Encoded Bodies sowie Datei-Uploads
- Responses in unterschiedlichen Formen lesen (`BodyHandlers.ofString/ofFile/ofLines/discarding`)
- Redirects und Authentifizierung bedienen
- Asynchrone Aufrufe mit `sendAsync` und `CompletableFuture` gestalten

## Hands-on-Aufgaben
1. Öffne `src/main/java/tech/erben/java17/httpclient/HttpClientWorkshop.java` und implementiere die Methoden, die aktuell `UnsupportedOperationException` werfen.
2. Orientiere Dich an `src/test/java/tech/erben/java17/httpclient/HttpClientWorkshopTest.java`. Jeder Test beschreibt einen typischen Anwendungsfall (Plaintext abholen, JSON posten, Datei laden/hochladen, Redirects folgen, Async-Calls etc.).
3. Nutze gezielt verschiedene `BodyPublisher`- und `BodyHandler`-Varianten, setze sinnvolle Header und Zeitlimits und halte den `HttpClient` über alle Aufrufe hinweg wiederverwendbar.
4. Wenn alles korrekt ist, werden die Tests grün.

## Starten
Die Tests laufen nur in diesem Modul:
```bash
mvn -pl assignments/http-client-api test -DskipTests=false
```

Oder direkt in der IDE aus `HttpClientWorkshopTest`.
