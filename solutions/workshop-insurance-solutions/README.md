# Schadenmanagement – Sealed-Class-Lösung

Diese Variante demonstriert eine vollständig ausgearbeitete Sealed-Class-Hierarchie: Alle Schadenarten implementieren das sealed Interface `Claim`, die Bewertungsergebnisse bestehen ebenfalls aus einer sealed Struktur `Assessment`.

## Highlights

- Sealed Interfaces + Records mit domänenspezifischen Enums (`Claim`, `MotorClaim`, `HomeClaim` usw.).
- Bewertungslogik via `switch` über die sealed Hierarchie – Compiler prüft vollständige Abdeckung.
- UI zeigt je nach Claim-Typ individuelle Details und das passende Assessment.

## Ausprobieren

```bash
mvn -pl solutions/sealed-classes-insurance-solutions spring-boot:run
```

Port: 9184

Vergleiche das Verhalten mit dem Übungsmodul, um die Vorteile der abgeschlossenen Hierarchie und der reinen Rückgabeobjekte zu sehen.
