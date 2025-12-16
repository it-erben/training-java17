package tech.erben.gfu.records.legacy;

import java.util.Objects;

public class MailingAddress {
    private String street;
    private String postalCode;
    private String city;
    private String country;

    public MailingAddress(String street, String postalCode, String city, String country) {
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public void updateCity(String newCity) {
        this.city = newCity;
    }

    public MailingAddress copy() {
        return new MailingAddress(street, postalCode, city, country);
    }

    public MailingAddress copyWithPostalCode(String newPostalCode) {
        return new MailingAddress(street, newPostalCode, city, country);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MailingAddress that)) return false;
        return Objects.equals(street, that.street)
                && Objects.equals(postalCode, that.postalCode)
                && Objects.equals(city, that.city)
                && Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, postalCode, city, country);
    }

    @Override
    public String toString() {
        return "MailingAddress{" +
                "street='" + street + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
