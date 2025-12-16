# Text Blocks Warmup – Mehrzeilige Strings ohne Escape-Salat

Dieses Modul enthält typische Anwendungsfälle für Java Text Blocks: HTML-Mail-Templates, SQL-Strings und Markdown-Fragmente, die per `+` und `\n` zusammengesetzt werden. Deine Aufgabe ist es, sie in Text Blocks zu überführen – inklusive Einrückung, Platzhaltern und String-Manipulationen.

## Aufgaben in Etappen
1. **HTML-Mails bereinigen**  
   - Wandle `EmailComposer#buildWelcomeMail` in einen Text Block um. Nutze Platzhalter (`%s`/`String#formatted`) statt Verkettungen.  
   - Achte darauf, dass `<a href>`-Zeilen sauber eingerückt sind und über `stripIndent()` o. Ä. keine überflüssigen Spaces in den Mail-Body rutschen.  
   - Erzeuge für den Reminder eine Variante, die die Kurs-Startzeit dynamisch formatiert (z. B. `DateTimeFormatter`).
2. **SQL lesbarer machen**  
   - Forme `SqlTemplates#selectActiveRegistrations` und `#insertAuditLog` zu Text Blocks. Nutze `stripIndent()` oder `indent()` sinnvoll, damit die Queries kopierbar sind.  
   - Baue für den `IN`-Filter eine `String#formatted`-Lösung oder `join`, die Parameter sauber einsetzt, ohne SQL-Injection zu riskieren (Platzhalter `?` behalten).  
   - Füge eine mehrzeilige Kommentarzeile in das Text Block ein, um zu üben, wie Backslashes und Quotes aussehen.
3. **Markdown aufräumen**  
   - Ersetze die Konkatenation in `MarkdownRenderer#renderReleaseNotes` durch einen Text Block mit eingebetteter Liste (`- Feature: ...`).  
   - Nutze `stripIndent()` um Bullet-Points richtig zu trimmen und lasse dabei bewusst eine leere Zeile zwischen Header und Inhalt.  
   - Ergänze eine kleine Tabelle (Header + eine Datenzeile), die zeigt, wie Pipes in Text Blocks behandelt werden.
4. **Playground anpassen**  
   - Aktualisiere `TextBlockPlayground`, so dass er die neuen Methoden (mit Text Blocks) nutzt. Baue ggf. kleine ASCII-Separatoren ein, die per Text Block erstellt werden.  
   - Führe die `main`-Methode aus und prüfe, ob die Einrückung und Leerzeilen passen.
