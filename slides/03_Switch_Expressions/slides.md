---
marp: true
theme: default
header: Java 14: Switch Expressions
footer: Alexander Erben
paginate: true
---
<style>
img[alt~="center"] {
  display: block;
  margin: 0 auto;
}
</style>

# Switch Expressions

---

## Bisher

Switch orientierte sich vorher an klassischen C-Style Switch. Hier ein Beispiel mit if.

```java
public String exampleOfIF(String animal) {
    String result;
    if (animal.equals("DOG") || animal.equals("CAT")) {
        result = "domestic animal";
    } else if (animal.equals("TIGER")) {
        result = "wild animal";
    } else {
        result = "unknown animal";
    }
    return result;
}
```

---

<style scoped>
pre {
   font-size: 0.6rem;
}
</style>
## Bisher

Dies entspricht mit klassischem Switch diesem Beispiel:

```java
public String exampleOfSwitch(String animal) {
    String result;
    switch (animal) {
        case "DOG":
            result = "domestic animal"; 
            break;
        case "CAT":
            result = "domestic animal";
            break;
        case "TIGER":
            result = "wild animal";
            break;
        default:
            result = "unknown animal";
            break;
    }
    return result;
}
```

---

<style scoped>
pre {
   font-size: 0.7rem;
}
</style>
## Risiko

Es passiert schnell, das man das _break_ vergisst.

```java
public String exampleOfSwitch(String animal) {
    String result;
    switch (animal) {
        case "DOG":
            result = "domestic animal";
        case "CAT":
            result = "domestic animal";
        case "TIGER":
            result = "wild animal";
        default:
            result = "unknown animal";
    }
    return result;
}
```

---

## Mit Switch Expressions

Seit Java 12 kann man Switch als Expression verwenden.

```java
public String exampleOfSwitch(String animal) {
    return switch (animal) {
        case "DOG", "CAT" ->
            "domestic animal";
        case "TIGER" ->
            "wild animal";
        default ->
            "unknown animal";
    };
}
```

--- 

## Yield-Keyword

Man kann auch komplexere Blöcke von Anweisungen verwenden und das Ergebnis mit 'yield' kommunizieren.

```java
var result = switch (month) {
    case JANUARY, JUNE, JULY -> 3;
    case FEBRUARY, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER -> 1;
    case MARCH, MAY, APRIL, AUGUST -> {
        int monthLength = month.toString().length();
        yield monthLength * 4;
    }
    default -> 0;
};
```

---

## Switch Expressions: Exhaustiveness

Anders als bei Switch Statements müssen bei Expressions alle Fälle abgedeckt werden (exhaustiveness).

```java
var result = switch (month) {
    case JANUARY, JUNE, JULY -> 3;
}; // compile error
```