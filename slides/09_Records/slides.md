---
marp: true
theme: default
header: Java 17: Records
footer: Alexander Erben
paginate: true
---

<style>
img[alt~="center"] {
  display: block;
  margin: 0 auto;
}
</style>

# Records

---

## Records: Überblick

* Records sind eine Sprachfunktion für „Daten-als-Daten“:
* kompakte, shallow‑immutable Klassen mit automatisch generierten
    * equals, hashCode, toString
    * Accessor‑Methoden
    * kanonischen (bzw. kompakten) Konstruktor.
* Seit JDK 16 final, im LTS JDK 17 alltagstauglich verfügbar.

---

## Records: Einfaches Beispiel

Ein erster Eindruck lässt sich gut an einem einfachen Beispiel gewinnen.

```java
public record Person(String firstName, String lastName, int age) {}
// Nutzung
var p = new Person("Ada", "Lovelace", 36);
System.out.println(p.firstName()); // "Ada"
System.out.println(p);             // Person[firstName=Ada, lastName=Lovelace, age=36]
```

In einer Zeile wird hier festgelegt, dass eine `Person` aus einem Vornamen, einem Nachnamen und einem Alter besteht. Der Compiler leitet daraus Felder, Accessor‑Methoden, `equals`, `hashCode` und `toString` ab.

---

## Records

* Komponenten werden zu private final Feldern und public Accessors.
* equals/hashCode vergleichen den Komponentenwertzustand; toString listet Namen und Werte.
* Records sind implizit final und erweitern java.lang.Record.
* Keine zusätzlichen nicht‑statischen Instanzfelder erlaubt (neben den Komponenten).
* Dürfen Interfaces implementieren, generisch sein und verschachtelte/lokale Records definieren.

---

## Records: Umfangreicheres Beispiel

```java
public record Rectangle(double length, double width) {}
// Der Compiler erzeugt ungefähr:
public final class Rectangle extends java.lang.Record {
  private final double length, width;
  public Rectangle(double length, double width) { this.length = length; this.width = width; }
  public double length() { return length; }
  public double width()  { return width; }
  @Override public boolean equals(Object o) { /* Komponentenvergleich */ }
  @Override public int hashCode() { /* aus Komponenten */ }
  @Override public String toString() { return "Rectangle[length=" + length + ", width=" + width + "]"; }
}
```

---

## Records: Kanonischer Konstruktor


Der kanonische Konstruktor entspricht in seiner Parameterliste exakt dem Header und bietet volle Kontrolle – inklusive Normalisierung:

```java
public record Email(String localPart, String domain) {
  public Email(String localPart, String domain) {
    this.localPart = Objects.requireNonNull(localPart).toLowerCase(Locale.ROOT);
    this.domain    = Objects.requireNonNull(domain).toLowerCase(Locale.ROOT);
  }
}
```

---

## Records: Kompakter Konstruktor

Für viele Anwendungsfälle genügt ein kompakter Konstruktor. Er verzichtet auf eine explizite Parameterliste und eignet sich besonders für Validierung:

```java
public record OrderId(long value) {
  public OrderId {
    if (value <= 0) throw new IllegalArgumentException("id must be > 0");
  }
}
```

---

# Records: Use Cases

---

## DTOs, API‑Antworten und Events

* Ein Einsatzbereich für Records sind einfache Datenübertragungsobjekte (DTOs), 
* Besonders hilfreich: Bei Erweiterung der Klasse kann man nicht vergessen, equals und hashCode anzupassen.

```java
public record UserDto(UUID id, String name) {}
public record UserCreatedEvent(UserDto user, Instant occurredAt) {}
```



---


## Konfigurations‑Snapshots

* Häufig sollen Konfigurationen „eingefroren“ und als unveränderliche Sicht weitergereicht werden. 
* Records eignen sich hierfür, insbesondere in Kombination mit defensiven Kopien:

```java
public record AppConfig(Map<String, String> props) {
  public AppConfig {
    this.props = Map.copyOf(props); // shallow immutability schützen
  }
}
```

---

## Werteobjekte und Identifier

Auch fachliche Werteobjekte – zum Beispiel Geldbeträge oder Identifier – lassen sich mit Records prägnant modellieren:

```java
public record Money(BigDecimal amount, Currency currency) {
  public Money {
    Objects.requireNonNull(amount);
    Objects.requireNonNull(currency);
    if (amount.scale() > 2) amount = amount.setScale(2, RoundingMode.HALF_UP);
    this.amount = amount;
    this.currency = currency;
  }
}
```

---


## Schlüssel in Maps

Da Records stabile `equals`/`hashCode`‑Implementierungen besitzen, sind sie für zusammengesetzte Schlüssel geeignet:

```java
record Point(int x, int y) {}
Map<Point, String> labels = Map.of(new Point(1, 2), "A");
```

---

## Wann Records nicht die beste Wahl sind

- Persistente Entities in ORMs wie JPA/Hibernate, die Mutabilität, Proxies oder no‑arg‑Konstruktoren erwarten.
- Reichhaltige Domänenobjekte mit umfangreichem Verhalten und komplexen Invarianten, bei denen der Fokus nicht auf einem reinen Datenzustand liegt.
- Tiefe, veränderliche Objektgraphen (Listen, Maps, Arrays), bei denen ohne defensive Kopien eine scheinbare „Unveränderlichkeit“ missverständlich wäre.