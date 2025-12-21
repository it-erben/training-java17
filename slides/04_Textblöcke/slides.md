---
marp: true
theme: default
header: Java 15: Textblöcke
footer: Alexander Erben
paginate: true
---
<style>
img[alt~="center"] {
  display: block;
  margin: 0 auto;
}
</style>

# Textblöcke

---

## Textblöcke

* Textblöcke sind mehrzeilige String‑Literale, eingeleitet und beendet durch `"""`.
* Sie wurden in JDK 15 finalisiert (nach Previews in JDK 13/14) und erleichtern das Einbetten von JSON/SQL/HTML
* Finalisiert wurden Textblöcke im [JEP 378](https://openjdk.org/jeps/378)

---

<style scoped>
pre {
   font-size: 0.5rem;
}
</style>

## Motivation: Vorher vs. heute

```java
String jsonOld = "{\n" +
"  \"name\": \"Ada\",\n" +
"  \"city\": \"London\"\n" +
"}";
```

```java
String jsonNew = """
{
  "name": "Ada",
  "city": "London"
}
""";
```

---

<style scoped>
pre {
   font-size: 0.6rem;
}
</style>

## Syntax

Textblöcke werden mit drei Anführungszeichen definiert:

```java
String poem = """
The time has come, the Walrus said,
To talk of many things...
""";
```

Dabei dürfen Multiline-Strings nicht einzeilig sein. Folgendes Beispiel funktioniert ***nicht***:

```java
String x = """Hi""" // kompiliert nicht;
```

---

## Erste Zeile

* Der Zeilenumbruch direkt nach `"""` gehört nicht zum Inhalt.
* Eine explizite Leerzeile am Anfang erzeugt dagegen ein führendes `\n`.

```java
String normal = """
Hello
World
"""; // "Hello\nWorld\n"

String withBlank = """

Hello
World
"""; // "\nHello\nWorld\n"
```

---

## Zeilenumbrüche

Abschließende Newlines werden in den String übernommen.

```java
String withNewline = """
a
b
c
"""; // entspricht "a\nb\nc\n"

String withoutNewline = """
a
b
c"""; // entspricht "a\nb\nc"
```

---

## Escape-Sequenz: Zeilenumbruch

Backslashes unterdrücken Zeilenumbrüche im String.

```java
String text = """
Lorem ipsum dolor sit amet, \
consectetur adipiscing elit, \
sed do eiusmod tempor. \
"""; // Wird zu einer Zeile.
```

---

## Geschützte Leerzeichen

Mit `\s` können Leerzeichen am Ende einer Zeile geschützt werden.

```java
String colors = """
red   \s
green \s
blue. \s
"""; 
// Alle Zeilen sind gleich lang!
```

---

## Anführungszeichen & Escapes

* Übliche Escapes gelten weiter (`\\t`, `\\n`, `\\\"`, `\\\\`).
* `\"\"\"` ermöglicht `"""` im Textblock.

```java
String quote = """
She said: \"Hello\"
Triple quotes: \"\"\"
""";
```

---

<style scoped>
pre {
   font-size: 0.4rem;
}
</style>

## Indentierung

Die Einrückung orientiert sich an den terminierenden Anführungszeichen.

```java
System.out.println("""
Guten Morgen, liebe Sorgen,
seid ihr auch schon wieder da.
""");

System.out.println("""
    Guten Morgen, liebe Sorgen,
    seid ihr auch schon wieder da.
""");

System.out.println("""
    Guten Morgen, liebe Sorgen,
    seid ihr auch schon wieder da.
    """);
```

```text
Guten Morgen, liebe Sorgen,
seid ihr auch schon wieder da.

    Guten Morgen, liebe Sorgen,
    seid ihr auch schon wieder da.

Guten Morgen, liebe Sorgen,
seid ihr auch schon wieder da.
```

---

<style scoped>
pre {
   font-size: 0.45rem;
}
</style>

## Typische Einsätze

```java
String sql = """
SELECT name, city
FROM customers
WHERE active = TRUE
""";
```

```java
String payload = """
{
  "name": "%s",
  "city": "%s"
}
""".formatted(name, city);
```

```java
String html = """
<div class="card">
  <p>%s</p>
</div>
""".formatted(text);
```

---

## Weitere neue Methoden (Auswahl)

```java
String template = """
Name: %s
City: %s
""".formatted("Ada Lovelace", "London"); // Wie String.format
```

```java
String raw = "Line1\\nLine2\\tTab";
String cooked = raw.translateEscapes(); // "Line1\nLine2\tTab"
```

---

## Hilfsmethoden für Whitespace

```java
String messy = "    a\n    b\n";
String normalized = messy.stripIndent(); // "a\nb\n"
String indented = normalized.indent(2);  // rückt beide Zeilen ein
String trimmed = messy.stripTrailing();  // ohne abschließende Spaces/Newlines
```

```java
String sql = """
    SELECT * 
    FROM users
    WHERE id = %d
    """.formatted(42).stripIndent();
```

---

## Dos & Don'ts

* Gut: Vorlagen für SQL/HTML/JSON, mehrzeilige Fehlermeldungen, ASCII‑Art, Multiline‑Regex.
* Ebenfalls gut: `.formatted(...)` direkt am Textblock für Templates.
* Vorsicht bei Zeilenendungen/Indentierung, wenn Leerzeichen semantisch relevant sind.
