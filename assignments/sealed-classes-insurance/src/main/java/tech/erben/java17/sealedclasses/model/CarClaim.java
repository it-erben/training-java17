package tech.erben.java17.sealedclasses.model;

import java.math.BigDecimal;

public record CarClaim(
        String policyNumber,
        BigDecimal amount,
        String damageDescription,
        boolean drivable
) implements Claim {
}
