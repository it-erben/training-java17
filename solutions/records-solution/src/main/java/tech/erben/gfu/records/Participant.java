package tech.erben.gfu.records;

import java.util.Objects;

public record Participant(String firstName,
                          String lastName,
                          String email,
                          String company) {

    public Participant {
        firstName = normalize("firstName", firstName);
        lastName = normalize("lastName", lastName);
        company = normalize("company", company);
        email = normalize("email", email).toLowerCase();
    }

    public Participant withEmail(String newEmail) {
        return new Participant(firstName, lastName, newEmail, company);
    }

    public String fullName() {
        return firstName + " " + lastName;
    }

    private static String normalize(String label, String value) {
        Objects.requireNonNull(value, label + " must not be null");
        return value.trim();
    }
}
