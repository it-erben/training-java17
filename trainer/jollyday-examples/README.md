# Jollyday Examples

Kleines Modul, das zeigt, wie mit [Jollyday](https://github.com/svendiedrichsen/jollyday) die bayerischen Feiertage berücksichtigt werden können, um Werktage zu zählen.

## Idee
- Das Modul nutzt den `HolidayCalendar.GERMANY` und filtert mit dem Regionscode `by` (Bayern).
- `WorkdayCalculator` zählt standardmäßig nur Werktage (Mo-Fr), die nicht auf einen bayerischen Feiertag fallen.
- `workingDaysSince(LocalDate referenceDate)` zählt vom Tag **nach** dem Referenzdatum bis heute (inklusive).
- `workingDaysBetween(LocalDate startInclusive, LocalDate endInclusive)` zählt beide Grenzen mit; liegt der Start nach dem Ende, wird `0` zurückgegeben.

## Beispiel
```java
WorkdayCalculator calculator = new WorkdayCalculator();

int days = calculator.workingDaysBetween(
        LocalDate.of(2024, 4, 30),
        LocalDate.of(2024, 5, 3));
// Liefert 3, da der 1. Mai 2024 (Feiertag) übersprungen wird.

int sinceProjectStart = calculator.workingDaysSince(LocalDate.of(2024, 1, 15));
```
