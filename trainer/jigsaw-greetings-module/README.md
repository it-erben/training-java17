# Jigsaw Greetings Module

Minimales Java-9-Modul (`tech.erben.gfu.jigsaw.greetings`), das eine kleine Greeting-API exportiert.

- `module-info.java` exportiert das Paket `tech.erben.gfu.jigsaw.greetings`.
- `GreetingService` stellt Fabrikmethoden für deutsche/englische Anreden bereit.

Build:

```bash
mvn -pl trainer/jigsaw-greetings-module package
```

Run:

```bash
java --module-path trainer/jigsaw-greetings-module/target/classes:trainer/jigsaw-greetings-app/target/classes -m tech.erben.gfu.jigsaw.app/
tech.erben.gfu.jigsaw.app.ConsoleApp "Ada Lovelace"
```
