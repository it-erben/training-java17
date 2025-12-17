# Schadenmanagement mit Sealed Classes

Dieses Modul zeigt, wie sealed Interfaces die erlaubten Schadenstypen einer Versicherung absichern.

## Lernziele
- Sealed Interfaces in Java 17 definieren
- Implementierungen per `permits` einschränken
- Mit sealed Hierarchien Services und UIs strukturieren

## Hands-on-Aufgaben
1. Füge einen weiteren Schadenstyp `HomeClaim` hinzu und passe den Service sowie das UI an.
2. Ergänze Validierungen, die pro Schadenstyp spezielle Grenzen für `amount` prüfen.
3. Schreibe Tests, die sicherstellen, dass unbekannte Implementierungen nicht zugelassen werden.
4. Diskutiere im Team, wann sealed Klassen gegenüber klassischen abstrakten Klassen Vorteile bieten.

## Link
Die Anwendung ist nach dem Start verfügbar unter:
http://localhost:9084
