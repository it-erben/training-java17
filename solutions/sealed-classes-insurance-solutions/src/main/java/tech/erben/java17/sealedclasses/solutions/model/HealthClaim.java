package tech.erben.java17.sealedclasses.solutions.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record HealthClaim(
        String policyNumber,
        BigDecimal amount,
        LocalDate reportedAt,
        String diagnosis,
        TreatmentType treatmentType,
        boolean inpatient
) implements Claim {

    public HealthClaim {
        if (amount.signum() < 0) {
            throw new IllegalArgumentException("amount darf nicht negativ sein");
        }
    }

    @Override
    public ClaimType type() {
        return ClaimType.HEALTH;
    }

    public enum TreatmentType {
        SURGERY,
        REHAB,
        OUTPATIENT
    }
}
