package tech.erben.java17.records.model;

public class Customer {

    private String name;
    private String email;
    private String preferredVin;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPreferredVin() {
        return preferredVin;
    }

    public void setPreferredVin(String preferredVin) {
        this.preferredVin = preferredVin;
    }
}
