package tech.erben.gfu.records.legacy;

import java.util.Objects;

public class Participant {
    private String firstName;
    private String lastName;
    private String email;
    private String company;

    public Participant(String firstName, String lastName, String email, String company) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.company = company;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getCompany() {
        return company;
    }

    public void changeEmail(String newEmail) {
        this.email = newEmail;
    }

    public String fullName() {
        return firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Participant participant)) return false;
        return Objects.equals(firstName, participant.firstName)
                && Objects.equals(lastName, participant.lastName)
                && Objects.equals(email, participant.email)
                && Objects.equals(company, participant.company);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, company);
    }

    @Override
    public String toString() {
        return "Participant{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", company='" + company + '\'' +
                '}';
    }
}
