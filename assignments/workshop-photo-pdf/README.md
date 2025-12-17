# Photo Lab – Upload als PDF zurück

Eine grössere Spring-Boot-Anwendung: Fotos werden im Browser hochgeladen, verarbeitet (Farbe/Graustufen/invertiert) und als PDF zurückgegeben. Der Code ist absichtlich noch nicht im modernen Java‑Stil.

## Lernziele
- Lokale Typdeklarationen durch `var` ersetzen und Lesbarkeit bewahren
- Mehrzeilige Texte als Text Blocks modellieren statt per String-Konkatenation
- Klassische `switch`-Statements in Switch Expressions überführen
- Bilder im Server verarbeiten und mit Metadaten in PDFs einbetten

## Hands-on-Aufgaben
1. Gehe den Code in `PhotoRenderService` und `ImageProcessingService` durch und ersetze dort (und in den Web-Schichten) sinnvolle lokale Typen durch `var`.
2. In `PhotoRenderService#buildMetadataBlock` wird der mehrzeilige Info-Block für das PDF per String-Konkatenation zusammengebaut – forme ihn zu einem modernen Text Block um.
3. `ImageProcessingService#applyColorMode` nutzt noch ein klassisches `switch`. Baue daraus eine Switch Expression mit klaren Defaults.
4. Starte die Anwendung und lade unterschiedliche Bilder hoch (Farbe/Graustufen/invertiert). Prüfe, ob die generierten PDFs den Textblock korrekt enthalten.

## Link
Die Anwendung ist nach dem Start verfügbar unter:
http://localhost:9086
