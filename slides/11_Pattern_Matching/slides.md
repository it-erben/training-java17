---
marp: true
theme: default
header: Java 17: Pattern Matching
footer: Alexander Erben
paginate: true
---

<style>
img[alt~="center"] {
  display: block;
  margin: 0 auto;
}
p > pre {
  font-size: 0.9rem
}
</style>

# Pattern Matching

---

## Pattern Matching in Java 17

In JDK 17 gibt es zwei Pattern‑Matching‑Fähigkeiten:
- Final: Pattern Matching for instanceof
- Preview: Pattern Matching for switch

---


## Ausgangspunkt

Stellen Sie sich vor, Sie übernehmen eine gewachsene Java‑Codebasis. Überall finden Sie Konstrukte wie:

```java
if (o instanceof String) {
    String s = (String) o;
    // ...
}
```

...oder tief verschachtelte `if‑else`‑Ketten, die verschiedene Subtypen eines gemeinsamen Interfaces behandeln. 

--- 

## Klassisches `instanceof`

* Das funktioniert – aber elegant ist das nicht. 
* Jeder Cast ist eine potentielle Fehlerquelle
* Pattern Matching for `instanceof` (JEP 394) ist seit JDK 16 final und in JDK 17 ganz normal nutzbar.
* Pattern Matching für `switch` (JEP 406) ist in JDK 17 noch ein Preview‑Feature, das Sie explizit mit `--enable-preview` aktivieren müssen. In neueren JDKs (ab 21) ist es final geworden.

---

## Vom klassischen instanceof zum Pattern Matching

* Beispiel aus vielen, älteren Projekten: Konfigurationen in Maps
* Sie werden zur Laufzeit aus einer `Map<String, Object>` geladen
* Ein Wert kann je nach Quelle ein `String`, ein `Integer` oder vielleicht auch `null` sein

```java
Object timeout = config.get("timeoutSeconds");
if (timeout instanceof Integer) {
    Integer t = (Integer) timeout;
    if (t > 0) {
        System.out.println("Timeout: " + t + "s");
    }
}
```

---
 
<style scoped>
pre {
   font-size: 0.58rem;
}
</style>

## Moderne Lösung
Mit Pattern Matching für `instanceof` reduziert sich dieser Code.

```java
if (timeout instanceof Integer t && t > 0) {
    System.out.println("Timeout: " + t + "s");
}

```
* `instanceof` akzeptiert nicht mehr nur einen Typ, sondern ein Typ‑Pattern wie `Integer t`. 
* Trifft das Pattern zu, steht Ihnen im if‑Block direkt die gebundene Variable `t` zur Verfügung – der explizite Cast entfällt.
---

## Flow Scope

* Der Compiler kennt den sogenannten _Flow Scope_
* Er weiß, in welchen Bereichen des Ausdrucks die Variable sicher gebunden ist. 
* Rechts von `&&` ist `t` garantiert vorhanden, rechts von `||` nicht – deshalb verweigert der Compiler dieses Beispiel:


```java
if (timeout instanceof Integer t || "1".equals(t.toString())) {
    // kompiliert nicht!
}
```

---

## Weiteres Beispiel


```java
@Override
public boolean equals(Object o) {
    return (o instanceof Money m)
        && amount.compareTo(m.amount) == 0
        && currency.equals(m.currency);
}
```

* Früher stand hier meist eine Kombination aus `if (!(o instanceof Money)) return false;` und einem expliziten Cast. 
* Mit Pattern Matching liest sich die Methode fast wie eine fachliche Aussage

---

## Pattern Matching im `switch`

Pattern Matching für `switch` ermöglicht einfache Unterscheidung nach Typ

```java
static String formatForLog(Object value) {
    return switch (value) {                    // Switch-Expression
        case Integer i -> "int=%d".formatted(i);
        case Long    l -> "long=%d".formatted(l);
        case Double  d -> "double=%f".formatted(d);
        case String  s -> "String=\"" + s + "\"";
        case null       -> "<null>";           // explizites null-Label
        default         -> value.toString();
    };
}
```

---

## Switch Expression

- `switch` ist hier eine _Expression_, d.h. der gesamte Ausdruck liefert einen Wert, der direkt zurückgegeben oder einer Variable zugewiesen werden kann.
- Die `case`‑Labels sind nicht mehr auf Konstanten beschränkt, sondern können Typ‑Patterns enthalten, z.B. `case String s`.
- `null` kann (und sollte) explizit behandelt werden. Ohne ein `case null`‑Arm verhält sich ein Pattern‑`switch` bei `null` wie das klassische `switch`: Es fliegt eine `NullPointerException`.


---

## `yield`-Keyword

Für den Fall, dass in einem Zweig einer Switch Expression mehrere Statements ausgeführt werden, kennzeichnet `yield` das Ergebnis.

```java
int value = switch (greeting) {
    case "hi" -> {
        System.out.println("I am not just yielding!");
        yield 1;
    }
    case "hello" -> {
        System.out.println("Me too.");
        yield 2;
    }
    default -> {
        System.out.println("OK");
        yield -1;
    }
};
```

---

## Guarded Patterns

Oft reicht der Typ allein nicht aus, um zu entscheiden, welche Logik greifen soll. 

```java
static String describeOrder(Order order) {
    return switch (order) {
        case OnlineOrder o when (o.total() > 100) -> "Große Online-Bestellung";
        case OnlineOrder o                        -> "Kleine Online-Bestellung";
        default                                   -> "Andere Bestellart";
    };
}
```

Erst wenn der Typ passt **und** die Bedingung erfüllt ist, greift der entsprechende `case`‑Arm.

---

## Dominanz-Prüfung

* Der Compiler kann nun auf Dominanz achten. 
* Er verhindert also, dass Sie einen `case`‑Arm schreiben, der nie erreicht werden kann.

```java
class Logger {
    void log(Object value) {
        switch (value) {
            case CharSequence cs -> System.out.println("Text: " + cs);
            case String s        -> System.out.println("String: " + s); // FEHLER: dominiert
            default              -> System.out.println("Sonstiger Typ: " + value);
        }
    }
}
```

---

## Erschöpfende Switches

Pattern Matching mit Switch lässt sich mit `sealed`-Hierarchiven verbinden.
```java
sealed interface Notification permits EmailNotification, SmsNotification, PushNotification {}
record EmailNotification(String address, String text) implements Notification {}
record SmsNotification(String number, String text) implements Notification {}
record PushNotification(String deviceId, String text) implements Notification {}

static int costInCents(Notification notification) {
    return switch (notification) {
        case EmailNotification e -> 1;
        case SmsNotification s   -> 5;
        case PushNotification p  -> 2;  // kein default nötig
    };
}
```

Der Compiler weiß, dass `Notification` nur diese drei Varianten haben darf. Deshalb akzeptiert er hier einen `switch` ohne `default` und prüft, dass alle Varianten abgedeckt sind.
