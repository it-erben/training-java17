package tech.erben.java17.sealedclasses.model;

import java.math.BigDecimal;

public record HealthClaim(
        String policyNumber,
        BigDecimal amount,
        String diagnosis,
        boolean inpatient
) implements Claim {
}
