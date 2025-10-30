package tech.erben.java17.sealedclasses.model;

import java.math.BigDecimal;

public record TravelClaim(
        String policyNumber,
        BigDecimal amount,
        String incidentCountry,
        boolean documentationComplete
) implements Claim {
}
