# Autohaus-Inventory – Record-Lösung

Diese Variante zeigt eine komplett durchgeführte Refaktorierung auf Records. Alle Domänenobjekte sind unveränderlich, notwendige Anpassungen erfolgen über `with`-Methoden oder berechnete Kopien.

## Highlights
- Domänenlogik nutzt Record-Konstruktoren für Validierung und Normalisierung (z. B. Preisskalierung).
- Services erzeugen neue Objekte statt Seiteneffekte an bestehenden Instanzen.
- Thymeleaf greift direkt auf Record-Zugriffsmethoden zu und erhält unveränderliche Listen/Maps.

## Ausprobieren
```bash
mvn -pl solutions/records-car-dealership-solution spring-boot:run
```
Port: 9181

Starte das Modul und vergleiche das Verhalten mit der Ausgangsversion – Rabatte, Leasing-Angebote und Probefahrten werden nun rein funktional verarbeitet.
