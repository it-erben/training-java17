package tech.erben.java17.records.solution.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record Car(
        String vin,
        String brand,
        String model,
        LocalDate firstRegistration,
        BigDecimal price,
        EnergyType energyType,
        SalesConsultant consultant,
        List<String> notes
) {

    public Car {
        Objects.requireNonNull(vin, "vin darf nicht null sein");
        Objects.requireNonNull(brand, "brand darf nicht null sein");
        Objects.requireNonNull(model, "model darf nicht null sein");
        Objects.requireNonNull(firstRegistration, "firstRegistration darf nicht null sein");
        Objects.requireNonNull(price, "price darf nicht null sein");
        Objects.requireNonNull(energyType, "energyType darf nicht null sein");
        price = price.setScale(2, RoundingMode.HALF_UP);
        notes = List.copyOf(notes == null ? List.of() : notes);
    }

    public Car withPrice(BigDecimal newPrice) {
        Objects.requireNonNull(newPrice, "newPrice darf nicht null sein");
        return new Car(vin, brand, model, firstRegistration, newPrice, energyType, consultant, notes);
    }

    public Car withConsultant(SalesConsultant newConsultant) {
        return new Car(vin, brand, model, firstRegistration, price, energyType, newConsultant, notes);
    }

    public Car withAddedNote(String note) {
        Objects.requireNonNull(note, "note darf nicht null sein");
        var updated = new ArrayList<>(notes);
        updated.add(note);
        return new Car(vin, brand, model, firstRegistration, price, energyType, consultant, updated);
    }

    public String label() {
        return "%s %s (%d)".formatted(brand, model, firstRegistration.getYear());
    }
}
