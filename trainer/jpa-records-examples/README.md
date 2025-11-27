# JPA Records Examples

Dieses Trainer-Modul demonstriert den Einsatz von Java Records in JPA:

- `Address` als `@Embeddable` Record, eingebettet in `Customer`
- `CustomerSummary` als DTO, das per JPQL `select new ...` direkt befüllt wird
- `RecordDemoService`, der Demo-Daten lädt und die Projektionen beim Start protokolliert

Starten kannst du das Modul mit:

```shell
mvn -pl trainer/jpa-records-examples spring-boot:run
```

Die Tests zeigen das Speichern des Embeddable Records sowie die DTO-Projektion:

```shell
mvn -pl trainer/jpa-records-examples test
```
