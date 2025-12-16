---
marp: true
theme: default
header: Java 10: Local Variable Type Inference
footer: Alexander Erben
paginate: true
---
<style>
img[alt~="center"] {
  display: block;
  margin: 0 auto;
}
section > h2 {
  position: absolute;
  top: 105px;
  left: 77px;
}
</style>

# Local Variable Type Inference
### "_var_"

---

## Local Variable Type Inference

Unter bestimmten Umständen kann der Compiler den Typ einer lokalen Variable automatisch ermitteln.
<hr>

_Aus_
```java
String message = "So war es früher"
```
_wird_
```java
var message = "So ist es seit Java 10"
```
---

## Vorteile

Das kann den Code lesbarer machen, in dem es das "Rauschen" durch Generics auf die rechte Seite der Zuweisung verschiebt.

<hr>

_Aus_
```java
Map<Integer, String> map = new HashMap<>();
```
_wird_
```java
var idToNameMap = new HashMap<Integer, String>();
```

---

## Einschränkungen

Der Compiler kann den Typ nicht ermitteln, wenn bei der keine vollständige Initialisierung stattfindet.

<hr>

```java
var i;          // Error: cannot use 'var' on variable without initalizer
var c = null;   // Error: Variable initializer is 'null'
```

---

## Einschränkungen

Auch für Felder, Lambda-Parameter und Arrays ist Local Variable Type Inference nicht möglich.

<hr>

```java
class Test {
    public var = "foo"; // error: 'var' is not allowed here
}


var p = (String s) -> s.length() > 10; // error: lambda expression needs an explicit target-type

var arr = { 1, 2, 3 }; // error: array initializer needs an explicit target-type
```

---

## Hinweise

Man sollte var nur einsetzen, wenn der Typ aus der rechten Seite der Zuweisung unmittelbar ersichtlich ist.

<hr>

```java
var result = service.execute(); // hier ist unklar, was der Return Type ist

var firstCustomerName = service.getCustomers().stream()
  .findFirst()
  .map(Customer::getUsername
  .orElse(0);
// bei langen Methodenketten ist var auch schlecht lesbar
```