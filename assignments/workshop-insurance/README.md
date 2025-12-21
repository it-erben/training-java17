# Schadenmanagement mit Sealed Classes

Dieses Modul zeigt, wie sealed Interfaces die erlaubten Schadenstypen einer Versicherung absichern.

## Lernziele

- Sealed Interfaces in Java 17 definieren
- Implementierungen per `permits` einschränken
- Mit sealed Hierarchien Services und UIs strukturieren

## Hands-on-Aufgaben

1. Füge einen weiteren Schadenstyp `HomeClaim` hinzu
2. `ClaimAssessmentService` umbauen, sodass er Switch Expressions verwendet
3. In `ClaimController` einen Beispieldatensatz für `HomeClaim` in die Liste einfügen
4. In `resources/templates/claims.html` eine weitere Zeile einfügen, die euren neuen `HomeClaim` anzeigt (Zeile 29)

## Link

Die Anwendung ist nach dem Start verfügbar unter:
<http://localhost:9084>
