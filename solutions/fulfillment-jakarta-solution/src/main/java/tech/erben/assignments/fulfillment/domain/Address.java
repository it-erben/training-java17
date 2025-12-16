package tech.erben.assignments.fulfillment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;

@Embeddable
public record Address(
    @NotBlank
    @Column(name = "street_line1")
    String line1,

    @Column(name = "street_line2")
    String line2,

    @NotBlank
    String city,

    @NotBlank
    String stateOrProvince,

    @NotBlank
    String postalCode,

    @NotBlank
    String country
) {

    public Address {
        line1 = trim(line1);
        line2 = trimToNull(line2);
        city = trim(city);
        stateOrProvince = trim(stateOrProvince);
        postalCode = trim(postalCode);
        country = trim(country);
    }

    private static String trim(String value) {
        return value != null ? value.trim() : null;
    }

    private static String trimToNull(String value) {
        return value != null && !value.isBlank() ? value.trim() : null;
    }

    // Bean-style getters keep JSF/JSON-B happy while we move to records
    public String getLine1() {
        return line1;
    }

    public String getLine2() {
        return line2;
    }

    public String getCity() {
        return city;
    }

    public String getStateOrProvince() {
        return stateOrProvince;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountry() {
        return country;
    }
}
