# Autohaus-Inventory – Ausgangspunkt für Records

Dieses Modull verwendet bewusst eine klassische, stark mutable Modellschicht. Ziel der Übung ist es, das Durcheinander aus Settern, Kopierlogik und geteilten Referenzen Schritt für Schritt in Records zu überführen.

## Lernziele
- Schmerzpunkte veränderlicher POJOs identifizieren
- Klassen in Records umwandeln und fehlende Copy-/With-Mechanismen ergänzen
- Service-Logik so refaktorieren, dass keine Seiteneffekte mehr nötig sind

## Hands-on-Aufgaben
1. Wandelt die Klassen und `SalesConsultant` `Customer` in ein Record um. Baut dabei sinnvolle Validierungen der Eingabedaten in den Konstruktor mit ein.
2. Wandelt  `LeasingOffer`, `TestDriveBooking` und `Car` in Records um (inklusive erforderlicher Validation).
3. Ersetzt die händische Kopierlogik in `InventoryService` durch saubere Kopier- oder `with`-Methoden.

## Link
Die Anwendung ist nach dem Start verfügbar unter:
http://localhost:9081
