**Java Streams Cheatsheet**

**Einführung**

* Die Streams-API ist eine neue Funktion in Java 8, die es ermöglicht, Daten in einem effizienten und eleganten Weise zu verarbeiten.
* Streams sind eine Reihe von Elementen, die nacheinander verarbeitet werden können.
* Streams können aus einer Vielzahl von Quellen erstellt werden, z. B. aus Arrays, Listen oder Collections.

**Intermediate Operations**

* Intermediate Operations transformieren die Elemente eines Streams.
* Sie kehren einen neuen Stream zurück, der die modifizierten Elemente enthält.

**Methode** | **Beschreibung**
---|---
`map()` | Transformiert jedes Element des Streams mit einer Funktion.
`flatMap()` | Transformiert jedes Element des Streams mit einer Funktion, die einen Stream zurückgibt.
`filter()` | Filtert die Elemente des Streams nach einer Bedingung.
`peek()` | Führt eine Aktion für jedes Element des Streams aus, ohne das Ergebnis zu verändern.
`limit()` | Erzeugt einen neuen Stream mit den ersten `n` Elementen des ursprünglichen Streams.
`skip()` | Erzeugt einen neuen Stream mit den Elementen des ursprünglichen Streams, beginnend mit dem `n`-ten Element.
`distinct()` | Entfernt alle doppelten Elemente aus dem Stream.
`sorted()` | Sortiert die Elemente des Streams.

**Terminal Operations**

* Terminal Operations beenden die Verarbeitung eines Streams und liefern ein Ergebnis zurück.

**Methode** | **Beschreibung**
---|---
`forEach()` | Führt eine Aktion für jedes Element des Streams aus.
`count()` | Ermittelt die Anzahl der Elemente im Stream.
`min()` | Ermittelt das kleinste Element im Stream.
`max()` | Ermittelt das größte Element im Stream.
`findAny()` | Ermittelt ein beliebiges Element im Stream.
`findFirst()` | Ermittelt das erste Element im Stream.
`allMatch()` | Prüft, ob alle Elemente des Streams einer Bedingung entsprechen.
`anyMatch()` | Prüft, ob mindestens ein Element des Streams einer Bedingung entspricht.
`noneMatch()` | Prüft, ob kein Element des Streams einer Bedingung entspricht.

**Beispiele**

```java
// Erzeuge einen Stream aus einer Liste von Integern
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
Stream<Integer> stream = numbers.stream();

// Verwende die map()-Operation, um jeden Integer in einen String umzuwandeln
Stream<String> stringStream = stream.map(Integer::toString);

// Verwende die filter()-Operation, um alle Zahlen größer als 3 zu filtern
Stream<String> filteredStream = stringStream.filter(s -> Integer.parseInt(s) > 3);

// Verwende die forEach()-Operation, um die Elemente des Streams zu drucken
filteredStream.forEach(System.out::println);
```

**Weitere Informationen**

* Die Streams-API ist ein komplexes Thema. Weitere Informationen finden Sie in der JavaDoc: [https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html).

**Hinweis**

Dieses Cheatsheet enthält nur die wichtigsten Features und Methoden der Streams-API. Für eine vollständige Übersicht über die API besuchen Sie bitte die JavaDoc.