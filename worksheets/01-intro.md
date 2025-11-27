# Übung 1: Verschiedene Neuerungen in aktuellen Java-Versionen

## 1\. Szenario

Eine externe Schnittstelle benötigt einen JSON-Export unserer Patentdaten. Dabei muss anhand des Ländercodes (die ersten zwei Buchstaben der ID) dynamisch eine Bearbeitungsgebühr berechnet werden.

Wir wollen den Code so kompakt und lesbar wie möglich halten und somit _Boilerplate_ vermeiden.

## 2\. Aufgaben

### Schritt 1: Statische Daten erzeugen

Erstelle in einer `main`-Methode eine kleine "Datenbank" im Speicher.

* Nutze **Collection Factory Methods** (`Map.of` oder `List.of`), um eine Map mit Beispieldaten zu erstellen.

* **Schlüssel:** Patent-ID (z.B. "DE102024", "EP305011", "US998877").
aufg
* 
* **Wert:** Eine `Map` von Gebietsschemata ("Locale") zu den Titeln des Patents in verschiedenen Sprachen.

Beispiel als Pseudocode:

```
Map<String, Map<String, String>> patents:
    "DE102024" -> 
        [
            "de_DE" -> "Faltbarer Display", 
            "en_US" -> "Foldable Display", 
            "fr_FR" -> "écran pliable"
        ]
    "EP305011" -> 
        [
            "de_DE" -> "Langlebiger Akku", 
            "en_US" -> "Long-life battery", 
            "fr_FR" -> "batterie longue durée"
        ]
```
> Du kannst auch die `java.util.Locale`-Klasse verwenden als Schlüssel in der Map der Titel.

Nutze überall, wo es sinnvoll ist, **Local Variable Type Inference** (`var`) für die Variablen.

### Schritt 2: Gebührenrechnung

Iteriere über die Einträge der Map. Ermittle innerhalb der Schleife anhand der ersten beiden Buchstaben der Patent-ID (z. B. "DE", "EP") die Gebühr. Nutze dafür eine **Switch Expression** (kein altes `switch-case` mit `break`\!):

* `DE` -\> `60.00 Euro`

* `EP` (Europäisches Patent) -\> `120.00 Euro`

* `US`, `JP` -\> `250.00 Euro`

* Alles andere -\> `300.00 Euro`

* Speichere das Ergebnis in einer Variable (`var gebuehr`).

### Schritt 3: JSON-Erzeugung (Text Blocks)

Erzeuge für jeden Eintrag einen String im JSON-Format. Nutze dafür **Text Blocks** (`""" ... """`) und `String.formatted()`.
Das Format soll so aussehen:

```json
{
  "id": "DE102024",
  "titel": {
    "de_DE": "Faltbares Display",
    "en_US": "Foldable display"
  },
  "gebuehr": 60.00
}
```

### Schritt 4: Ergebnis ausgeben

Erzeuge ein JSON-Array aus den Strings und drucke es auf der Konsole.

```json

[
    {
      "id": "DE102024",
      "titel": {
        "de_DE": "Faltbares Display",
        "en_US": "Foldable display",
        "fr_FR": "Écran pliable"
      },
      "gebuehr": 60.00
    },
    {
      "id": "EP10233",
      "titel": {
        "de_DE": "Langlebige Batterie",
        "en_US": "Long life battery",
        "fr_FR": "Batterie longue durée"
      },
      "gebuehr": 120.00
    }
]
```
