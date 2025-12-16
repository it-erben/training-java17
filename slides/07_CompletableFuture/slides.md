---
marp: true
theme: default
header: Java 8: CompletableFuture
footer: Alexander Erben
paginate: true
---

<style>
img[alt~="center"] {
  display: block;
  margin: 0 auto;
}
</style>

# CompletableFuture

---

## CompletableFuture

* Asynchrone Berechnungen sind schwer nachvollziehbar – Ablauf nicht linear, Aktionen (Callbacks) oft verteilt oder verschachtelt.
* Fehlerhandling erschwert die Komplexität – Fehler können an beliebigen Stellen im Ablauf auftreten.
* Java 5: Future als erster Ansatz – liefert ein Ergebnis, bietet aber keine Möglichkeit, Berechnungen zu verketten oder Fehler elegant zu behandeln.

---

## CompletableFuture

* Java 8: Einführung von CompletableFuture – implementiert zusätzlich CompletionStage.
* CompletionStage beschreibt einen kombinierbaren asynchronen Berechnungsschritt.
* CompletableFuture als Baustein & Framework – rund 50 Methoden zum Verketten, Kombinieren, Ausführen und Fehlerbehandeln asynchroner Schritte.

---

## CompletableFuture


* CompletableFuture implementiert Future, erweitert es aber um aktive Vervollständigungslogik.
* Eine Instanz kann leer erzeugt und an Konsumenten verteilt werden; später wird sie über complete(...) abgeschlossen.
* Konsumenten können per get() blockierend auf das Ergebnis warten.
* Typisches Muster:
    * Methode erstellt ein CompletableFuture,
    * startet eine Berechnung in einem anderen Thread,
    * gibt das Future sofort zurück,
    * und ruft nach Abschluss der Berechnung complete(...) auf.

---

## Erzeugung eines CompletableFuture

Ein CompletableFuture ist nicht direkt nach seiner Anlage abgeschlossen. Die Ausführung übernimmt ein _Executor_. Wird keiner übergeben, greift der Standard-Executor-Pool.
```java
var executor = Executors.newCachedThreadPool();

var completableFuture = CompletableFuture.supplyAsync(() -> {
        sleep();
        return doWork();
    }, executor);

```

---

## Erzeugung eines CompletableFuture

Wir können auch direkt ein abschlossenes Future anlegen, wenn wir das Ergebnis bereits wissen. Dies hilft, wenn uns ein Interface vorgibt, dass wir ein CompletableFuture zurückgeben _müssen_.

```java
var completableFuture = 
  CompletableFuture.completedFuture("Hello");

// ...

String result = completableFuture.get();
```

---

## Weiterverwendung der Ergebnisse

```java
CompletableFuture<String> completableFuture
  = CompletableFuture.supplyAsync(() -> "Hello");

CompletableFuture<String> future = completableFuture
  .thenApply(s -> s + " World");

assertEquals("Hello World", future.get());
```

**thenApply**
Bei CompletableFuture werden Lambdas verwendet, um das Ergebnis weiterzuverarbeiten.

---

## Weiterverwendung der Ergebnisse

```java
var completableFuture
  = CompletableFuture.supplyAsync(() -> "Hello");

var future1 = completableFuture
  .thenAccept(s -> System.out.println("Computation returned: " + s)); // Für Consumer
  
var future2 = completableFuture
  .thenRun(() -> System.out.println("Computation finished.")); // FÜr Runnable
```
**thenAccept**
Wollen wir einen Java 8-Consumer verwenden, so verwenden wir diese Methode.

**thenRun**
Diese Methode erwartet ein Runnable als Parameter, welches als Lambda aber genau wie ein Consumer aussieht.
