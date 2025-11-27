package tech.erben.gfu.jparecords;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public record Address(
        @Column(name = "street") String street,
        @Column(name = "postal_code") String postalCode,
        @Column(name = "city") String city
) implements Serializable {
}
