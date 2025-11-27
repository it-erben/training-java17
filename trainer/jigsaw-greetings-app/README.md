# Jigsaw Greetings App

Kleine Konsolenanwendung, die das Modul `tech.erben.gfu.jigsaw.greetings` nutzt.

Build (inklusive Bibliothek):

```bash
mvn -pl trainer/jigsaw-greetings-app -am package
```

Start mit Modulpfad:

```bash
java --module-path \
  trainer/jigsaw-greetings-module/target/classes:trainer/jigsaw-greetings-app/target/classes \
  -m tech.erben.gfu.jigsaw.app/tech.erben.gfu.jigsaw.app.ConsoleApp "Ada Lovelace"
```
