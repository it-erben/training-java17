# Food Delivery Board – Switch Expressions & Pattern Matching

Dieses Modul enthält eine Anwendung für einen Essens-Lieferservice.

## Lernziele
- Switch Expressions nutzen, um Status-Meldungen, Badges und Emojis abzuleiten
- Pattern Matching mit `instanceof` einsetzen, um Ereignisse eines sealed Event-Modells zu beschreiben
- Records und Enums kombinieren, um ein reichhaltiges Domainmodell für Lieferungen zu gestalten

## Hands-on-Aufgaben
1. Ergänze weitere `DeliveryEvent`-Typen (z.B. `PaymentIssue`) und erweitere die Pattern-Matching-Logik.
2. Füge eine neue Switch Expression hinzu, die abhängig von der Entfernung verschiedene Lieferzonen beschreibt.
3. Implementiere einen Filter im Controller, der nur Lieferungen eines bestimmten Status anzeigt – nutze dabei Switch Expressions, um die Auswahl zu validieren.
4. Schreibe Tests, die sicherstellen, dass neue Events im `DeliveryEventNarrator` nicht vergessen werden.

## Starten
```bash
mvn -pl assignments/food-delivery-modern-java spring-boot:run
```
Port: 9083
