# Records Warmup – Schrittweise zu Java Records

Eine kleine Registrierungsdomäne führt dich in mehreren Etappen an Records heran. Der vorhandene Code nutzt noch klassische, mutable POJOs – deine Aufgabe ist es, ihn behutsam in idiomatische Records zu überführen und dabei typische Stolpersteine (Validierung, Kopieren, Zusammensetzen) zu üben.

## Was liegt bereit?
- `tech/erben/gfu/records/legacy/Participant`: klassischer Teilnehmer-POJO mit Setter, `fullName` und einer `changeEmail`-Methode.
- `tech/erben/gfu/records/legacy/MailingAddress`: Adresse mit Copy-Methoden, aber ohne echte Validierung.
- `tech/erben/gfu/records/legacy/WorkshopRegistration`: Aggregat aus Participant, Adresse, gebuchten Modulen und Preislogik.
- `tech/erben/gfu/records/RecordPlayground`: kleines Main-Programm, um die Klassen anzufassen und Verhalten zu beobachten.

## Aufgaben in Etappen
1. **Erstes Record bauen**  
   - Forme `Participant` in ein Record um. Der vorhandene Konstruktor darf in einen kompakten Konstruktor überführt werden. Füge ein, dass die Eingabewerte im Konstruktur getrimmt werden, nicht null sein dürfen und `email` auf Kleinbuchstaben normalisiert wird.  
   - Ersetze die mutierende `changeEmail`-Methode durch eine `withEmail`-Variante, die ein neues Record zurückliefert. `fullName` bleibt als zusätzliche Methode bestehen.
2. **Record mit Validierung + Factory**  
   - Wandle `MailingAddress` in ein Record mit kompaktem Konstruktor um. Validiere, dass `postalCode` fünf Zeichen hat und `country` immer in Großbuchstaben landet (Standard: `"DE"`).  
   - Baue eine statische Fabrik (`of` oder `from`), die Strings aufbereitet und in das Record einspeist. Die bestehenden Copy-Varianten sollten als `with...`-Methoden zurückkommen.
3. **Aggregate & defensive Kopien**  
   - `WorkshopRegistration` soll als Record die beiden oben genannten Records verwenden und die Modulliste (`bookedModules`) defensiv kopieren (`List.copyOf`).  
   - Implementiere passende `with`-Hilfsmethoden, z. B. `withAddedModule(String)` und `withPricePerModule(BigDecimal)`, die neue Instanzen zurückgeben. `calculateTotalPrice` und `summary` bleiben bestehen und nutzen das unveränderliche Modell.  
   - Achte darauf, dass `registeredAt` nie `null` ist (ggf. im Konstruktor auf `LocalDate.now()` zurückfallen).
4. **Playground aktualisieren**  
   - Passe `RecordPlayground` so an, dass er mit den neuen Records arbeitet. 
   - Bedenke dabei, dass Records unveränderlich sind.
