package tech.erben.java17.records.solution.model;

import java.util.Objects;

public record Customer(
        String name,
        String email,
        String preferredVin
) {

    public Customer {
        Objects.requireNonNull(name, "name darf nicht null sein");
        Objects.requireNonNull(email, "email darf nicht null sein");
    }

    public Customer withPreferredVin(String vin) {
        return new Customer(name, email, vin);
    }
}
