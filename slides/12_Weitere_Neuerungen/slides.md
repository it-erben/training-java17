---
marp: true
theme: default
header: Java 11-17: Weitere Neuerungen
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

# Weitere Neuerungen

---

## Enhanced NPEs

NullPointerExceptions waren bisher nicht sehr aussagekräftig

```java
String emailAddress = employee.getPersonalDetails().getEmailAddress().toLowerCase();

# Exception in thread "main" java.lang.NullPointerException
#  at main(Example.java:10)
```

Seit Java 17 enthalten NPEs die genaue Ursache

```
Exception in thread "main" java.lang.NullPointerException: 
  Cannot invoke "String.toLowerCase()" because the return value of 
"Main$PersonalDetails.getEmailAddress()" is null
  at main(HelpfulNullPointerException.java:10)
```

---

<style scoped>
pre {
   font-size: 0.5rem;
}
</style>

## Neue String-Methoden

```java
public class StringExamples {

    public static void main(String[] args) {

        // chars() & codePoints()  (seit Java 9 – in CharSequence)
        "Hi!".chars().forEach(System.out::println);
        "A&#x1f642;".codePoints().forEach(System.out::println);

        // isBlank()  (seit Java 11)
        System.out.println("  \n\t".isBlank());

        // strip(), stripLeading(), stripTrailing()  (seit Java 11)
        System.out.println("  Hallo  ".strip());

        // lines()  (seit Java 11)
        "A\nB\nC".lines().forEach(System.out::println);

        // repeat()  (seit Java 11)
        System.out.println("Ha".repeat(3));

        // indent()  (seit Java 12)
        System.out.println("line1\nline2".indent(4));

        // transform()  (seit Java 12)
        System.out.println("alex".transform(String::toUpperCase));
    }
}
```

---

# Entfall Finalization (Java 18)

---

## Entfall Finalization

Manche Ressourcen müssen nach Verwendung aufgeräumt werden.
Dies geht im finally-Block oder mit try-with-resources.

```java
public void copyFileOperationClassic() throws IOException {
    var fis = new FileInputStream("input.txt");
    try {
        // use file stream
    } finally {
        fis.close();
    }
}


public void copyFileOperationTWR() throws IOException {
    try(var fis = new FileInputStream("input.txt")) {
        // use file stream
    }
}
```

---

<style scoped>
pre {
   font-size: 0.5rem;
}
</style>

## Entfall Finalization

Aus den Urzeiten von Java gab es das Feature, das Objekte kurz vor ihrer Zerstörung durch den Garbage-Collector die finalize-Methode aufrufen.

```java
public class MyFinalizableResourceClass {
    FileInputStream fis = null;

    public MyFinalizableResourceClass() throws FileNotFoundException {
        this.fis = new FileInputStream("file.txt");
    }

    public int getByteLength() throws IOException {
        return this.fis.readAllBytes().length;
    }

    @Override
    protected void finalize() throws Throwable {
        fis.close();
    }
}
```
* Das war keine gute Idee, weil man nie genau weiß, wann die Methode aufgerufe wird.
* Es verlangsamt außerdem den Garbage Collector erheblich.

--- 

## Entfall Finalization

Seit Java 18 ist die finalize-Methode nicht mehr verfügbar. Sie wurde im Zuge von JEP 421 abgeschafft. Als Ersatz steht schon seit Java 7 das try-with-resources-Muster zur Verfügung.

```java
public void readFileOperationWithTryWith() throws IOException {
    try (FileOutputStream fis = new FileOutputStream("input.txt")) {
        // perform operations
    }
}
```
