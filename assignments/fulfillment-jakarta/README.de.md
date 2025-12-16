# Fulfillment Jakarta EE (Bootable WildFly)

Kurzanleitung auf Deutsch, ausgehend von `fulfillment-jakarta/` als Arbeitsverzeichnis.

## Starten

1) Bauen (mit lokalem Maven-Repo im Projektverzeichnis, damit es im Sandbox-Setup funktioniert):
```
mvn -DskipTests -Dmaven.repo.local=../.m2 package
```

2) Bootable JAR starten:
```
java -jar target/fulfillment-jakarta-bootable.jar
```

3) Aufrufen:
- REST: `http://localhost:8080/api/orders`, `http://localhost:8080/api/shipments`
- Admin-UI (JSF): `http://localhost:8080/admin.xhtml`
  - Login: `admin` / `admin123`
  - Button „Fill random demo data“ füllt die Maske, Seed-Daten werden beim ersten Start automatisch angelegt.

## Datenbank

- In-Memory H2 Datasource `java:jboss/datasources/FulfillmentDS`, wird per CLI-Script beim Booten provisioniert.
- Schema-Generierung steht auf `update` (siehe `META-INF/persistence.xml`).

## Module / Technologien

- Jakarta EE 10 (JAX-RS, JPA, CDI, JSF) auf WildFly Bootable JAR
- H2 für lokale Persistenz

## Nächste Schritte (optional)

- Security/Credentials anpassen (siehe `src/main/wildfly/security.cli`)
- Persistente DB anbinden (DataSource in `datasource.cli` + `persistence.xml` anpassen)
