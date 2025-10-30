# Java 17 Kursmodule

Dieses Repository bündelt alle Materialien für deinen Java‑17-Schulungstag. Du findest hier Übungen, Referenzlösungen und einen optionalen Playground zum Nachbauen der Live-Demo.

- `assignments/`: Ausgangsprojekte für deine Hands-on-Aufgaben.
- `solutions/`: Referenzimplementierungen mit denselben Use Cases.
- `trainer/`: Live-Coding-Playground, falls du die Demo im eigenen Tempo nachvollziehen möchtest.

Jedes Modul läuft auf einem eigenen Port (siehe jeweilige README). Alle Projekte basieren auf Spring Boot und setzen Java 17 voraus.

## Assignments (Ports 9081–9085)
- `assignments/records-car-dealership`: Records für ein Autohaus-Inventar.
- `assignments/text-blocks-barbershop`: Text Blocks für Terminbestätigungen.
- `assignments/food-delivery-modern-java`: Switch Expressions & Pattern Matching im Lieferdienst.
- `assignments/sealed-classes-insurance`: Sealed Interfaces zur Modellierung von Schadensfällen.
- `assignments/enhanced-npe-helpdesk`: SVG-Dashboard mit zahlreichen NullPointer-Bugs.

## Solutions (Ports 9181–9185)
- `solutions/records-car-dealership-solution`: Refaktorierte Record-Lösung.
- `solutions/text-blocks-barbershop-solution`: Erweiterte Text-Block-Beispiele (HTML, Markdown, SQL, Escapes).
- `solutions/food-delivery-modern-java-solution`: Zusätzliche Auswertungen, Events und Zonen.
- `solutions/sealed-classes-insurance-solutions`: Robuste Sealed-Class-Verarbeitung.
- `solutions/enhanced-npe-helpdesk-solution`: Null-sichere SVG-Generierung ohne NPEs.

## Live-Coding-Playground (Port 9190)
- `trainer`: Video-Streaming-Plattform zum Experimentieren mit Records, Sessions und dem HTML5-Player.

## Build & Smoke Tests
`mvn verify`: kompiliert alle Module; Tests laufen ausschließlich in den Lösungen.

## Nutzung
Einzelne Module starten:

```bash
mvn -pl <pfad/zum/modul> spring-boot:run
```

Die README-Dateien in `assignments/`, `solutions/` und `trainer/` enthalten Ports, Startbefehle und Aufgabenhinweise.
