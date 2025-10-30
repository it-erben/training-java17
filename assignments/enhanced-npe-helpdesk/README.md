# Helpdesk-SVG mit absichtlichen NullPointerExceptions

Dieses Modul erzeugt mit Apache Batik eine SVG-Ansicht des Helpdesk-Boards. Mehrere Fehler sorgen dafür, dass beim Rendern NullPointerExceptions auftreten – so kannst du die verbesserten NPE-Meldungen von Java 17 live analysieren.

## Lernziele
- Verbesserte NPE-Meldungen interpretieren und betroffene Variablen finden
- Schrittweise Null-Schutzmaßnahmen implementieren (Dokumenterzeugung, Ticketdaten, Farben, Fortschritt)
- Testgetrieben arbeiten: Für jeden der Bugs unten erst einen Unit-Test anlegen und danach beheben.

## Bekannte Bugs (bitte beheben)
1. `createDocument()` gibt `null` zurück – schon beim ersten Zugriff auf das SVG-Dokument kracht es.
2. Tickets mit fehlendem Betreff führen zu NPEs, sobald `toUpperCase()` aufgerufen wird (sowohl im Renderer als auch im Template).
3. Tickets ohne Fortschrittswert verursachen NPEs bei der Prozentberechnung und später im Balkendiagramm.
4. Tickets ohne zugewiesene Agent:innen, ohne Farbe oder ohne E-Mail liefern weitere NPEs bei der Farbberechnung und beim Anzeigen der Kontaktdaten.
5. Ein Ticket besitzt gar keine ID, was bei der Großschreibung im Renderer ebenfalls eine NPE auslöst.

## Starten
```bash
mvn -pl assignments/enhanced-npe-helpdesk spring-boot:run
```
Port: 9085
