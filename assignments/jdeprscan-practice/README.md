# jdeprscan – Übung

Dieses Modul enthält absichtlich veraltete API-Verwendungen, damit du den
JDK-Scanner `jdeprscan` ausprobieren kannst. Die Klassen unter
`tech.erben.java17.jdeprscan` nutzen unter anderem `SecurityManager`,
`Thread.stop/suspend/resume`, `Runtime.runFinalizersOnExit`,
`Observable/Observer` und `java.security.acl.Acl`.

## Lernziele

- `jdeprscan` auf ein Modul/Artifact anwenden (`--release`, `--for-removal`, `--verbose`)
- Ausgabe lesen und Fundstellen im Code nachverfolgen
- Ersatzlösungen recherchieren (z. B. Interrupts/ExecutorServices statt `Thread.stop`, `Cleaner` statt `runFinalizersOnExit`, Events via `Flow`/Listener statt `Observable`)
- Build-Schritte definieren, um Deprecated-APIs künftig automatisch zu erkennen

## Aufgaben

1. Modul bauen: `mvn -pl assignments/jdeprscan-practice package -DskipTests=true`
2. Fundstellen suchen: `jdeprscan --release 21 target/jdeprscan-practice-0.0.1-SNAPSHOT.jar`
3. Nur stark veraltete APIs prüfen: `jdeprscan --for-removal --release 21 target/jdeprscan-practice-0.0.1-SNAPSHOT.jar`
4. Jede Trefferstelle durch moderne Alternativen ersetzen. Hinweise:
   - Thread-Steuerung auf Interrupts/`ExecutorService` umstellen.
   - SecurityManager entfernen oder durch explizite Sicherheitsprüfungen ersetzen.
   - Finalizer-Aufrufe durch `Cleaner`/`AutoCloseable` ersetzen.
   - `Observable`/`Observer` durch eigene Listener oder `java.util.concurrent.Flow` ablösen.
   - `java.security.acl` durch aktuelle Berechtigungsmodelle (z. B. `java.security.Policy`, Rollen/Claims) ersetzen.
5. `jdeprscan` erneut ausführen, bis keine Treffer mehr auftauchen.

### Bonus

- `jdeprscan` in den Build integrieren (z. B. Maven-Exec-Plugin), sodass der Build bei `--for-removal`-Treffern fehlschlägt.
- Dokumentiere in einem eigenen Markdown-Abschnitt die gefundenen Alt-APIs und deine gewählte Migration.
