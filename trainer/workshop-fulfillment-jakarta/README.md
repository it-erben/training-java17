# Fulfillment Jakarta EE (Bootable WildFly)

## Starten

1) Bauen

```text
mvn package
```

1) Bootable JAR starten:

```text
java -jar target/fulfillment-jakarta-0.0.1-SNAPSHOT-bootable.jar
```

1) Aufrufen:

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
