# Autohaus-Inventory – Ausgangspunkt für Records

Dieses Modull verwendet bewusst eine klassische, stark mutable Modellschicht. Ziel der Übung ist es, das Durcheinander aus Settern, Kopierlogik und geteilten Referenzen Schritt für Schritt in Records zu überführen.

## Lernziele
- Schmerzpunkte veränderlicher POJOs identifizieren
- Klassen in Records umwandeln und fehlende Copy-/With-Mechanismen ergänzen
- Service-Logik so refaktorieren, dass keine Seiteneffekte mehr nötig sind

## Hands-on-Aufgaben
1. Wandelt `Car`, `Customer`, `SalesConsultant`, `LeasingOffer` und `TestDriveBooking` in Records um (inklusive erforderlicher Validation).
2. Ersetzt die händische Kopierlogik in `InventoryService` durch saubere Kopier- oder `with`-Methoden.
3. Sorgt dafür, dass Rabattberechnungen ohne temporäre Zustandsänderungen auskommen.
4. Schreibt Tests, die die neue Record-basierte Logik absichern (z.B. Discount-Berechnung, Leasing-Rate).

## Starten
```bash
mvn -pl assignments/records-car-dealership spring-boot:run
```
Port: 9081
