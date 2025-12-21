# Records – Lösung

Record-basierte Variante der Registrierungsdomäne aus dem Übungsmodul `assignments/records`.

## Highlights

- `Participant`, `MailingAddress` und `WorkshopRegistration` als Records mit Trimmen/Normalisierung, Validierung (PLZ, Country) und Default-Datum.
- `with...`-Hilfsmethoden für E-Mail-Updates, Adress-Änderungen und Modulliste (defensive Kopien via `List.copyOf`).
- `RecordPlayground` zeigt den immutablen Umgang (neue Instanzen statt Mutationen) und berechnet den Gesamtpreis.

## Ausprobieren

```bash
# Playground starten
mvn -pl solutions/records-solution -DskipTests exec:java \
  -Dexec.mainClass=tech.erben.gfu.records.RecordPlayground
```
