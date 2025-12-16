package tech.erben.gfu.records;

import java.util.Objects;

public record MailingAddress(String street,
                             String postalCode,
                             String city,
                             String country) {

    public MailingAddress {
        street = normalize("street", street);
        postalCode = normalizePostalCode(postalCode);
        city = normalize("city", city);
        country = normalizeCountry(country);
    }

    public static MailingAddress of(String street, String postalCode, String city, String country) {
        return new MailingAddress(street, postalCode, city, country);
    }

    public MailingAddress withPostalCode(String newPostalCode) {
        return new MailingAddress(street, newPostalCode, city, country);
    }

    public MailingAddress withCity(String newCity) {
        return new MailingAddress(street, postalCode, newCity, country);
    }

    public MailingAddress withStreet(String newStreet) {
        return new MailingAddress(newStreet, postalCode, city, country);
    }

    public MailingAddress withCountry(String newCountry) {
        return new MailingAddress(street, postalCode, city, newCountry);
    }

    private static String normalize(String label, String value) {
        Objects.requireNonNull(value, label + " must not be null");
        return value.trim();
    }

    private static String normalizePostalCode(String value) {
        String trimmed = normalize("postalCode", value);
        if (trimmed.length() != 5) {
            throw new IllegalArgumentException("postalCode must have exactly 5 characters");
        }
        return trimmed;
    }

    private static String normalizeCountry(String value) {
        String trimmed = value == null || value.isBlank() ? "DE" : value.trim();
        return trimmed.toUpperCase();
    }
}
