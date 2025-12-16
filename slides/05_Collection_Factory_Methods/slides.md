---
marp: true
theme: default
header: Java 9ff.: Collection Factory Methods
footer: Alexander Erben
paginate: true
---
<style>
img[alt~="center"] {
  display: block;
  margin: 0 auto;
}
</style>

# Collection Factory Methods

<style scoped>
pre {
   font-size: 0.6rem;
}
</style>

---

## Collection Factory Methods


Früher war das erstellen von Collections mit einer festen Anzahl an Werten sehr mühselig.

```java
Set<String> set = new HashSet<>();
set.add("d");
set.add("p");
set.add("m");
set.add("a");
set = Collections.unmodifiableSet(set);
```

---

## Collection Factory Methods

Das geht heute viel einfacher.

```java
Set.of("d", "p", "m", "a")
```

```java
List.of("d", "p", "m", "a")
```

--- 

## Fallstrick

Die entstehenden Collections sind **immutable** und **null ist nicht erlaubt**.

```java
List.of("d", "p", "m", "a").add("..."); 
// UnsupportedOperationException
```

```java
List.of(null, "d", "p", "m", "a");
// IllegalArgumentException
```

--- 

## Set-Factory Method

Außerdem wird bei der Set.of()-Methode eine IllegalArgumentException geworfen, wenn die Werte nicht einmalig sind.

```java
Set.of("d", "d" "p", "m", "a");
// IllegalArgumentException
```

---

## Map-Factory Method

Für Map gibt es ebenfalls eine Factory Method. Allerdings funktioniert dies nur bis 10 Einträge. Darüber hinaus wurde die Methode nicht implementiert.

```java
Map.of("companyName", "dpma", "streetAddress", " Zweibrückenstraße 12");
```