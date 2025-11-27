# Musterlösung: Java Records

## Erfinder.java

```java
import java.util.Objects;

public record Erfinder(String name, String email) {
    // Kompakter Konstruktor zur Validierung
    public Erfinder {
        Objects.requireNonNull(name, "Name darf nicht null sein");
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name darf nicht leer sein");
        }
    }
}
```

## PatentAnmeldung.java

```java
import java.time.LocalDate;
import java.util.Objects;

public enum Status { EINGEREICHT, GEPRUEFT, ERTEILT, ABGELEHNT }

public record PatentAnmeldung(
    String id, 
    String titel, 
    LocalDate einreichungsDatum, 
    Erfinder hauptErfinder, 
    Status status
) {
    
    // Kompakter Konstruktor
    public PatentAnmeldung {
        Objects.requireNonNull(id);
        Objects.requireNonNull(titel);
        
        if (einreichungsDatum.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Datum in der Zukunft");
        }
    }

    // Business-Logik
    public boolean istVeroeffentlichbar() {
        boolean istAltGenug = einreichungsDatum
                .plusMonths(18)
                .isBefore(LocalDate.now());
                
        return status == Status.ERTEILT && istAltGenug;
    }

    // "Wither"-Methode
    public PatentAnmeldung mitStatus(Status neuerStatus) {
        return new PatentAnmeldung(
            this.id, 
            this.titel, 
            this.einreichungsDatum, 
            this.hauptErfinder, 
            neuerStatus
        );
    }
}
```
