package tech.erben.java17.sealedclasses.solutions.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record HomeClaim(
        String policyNumber,
        BigDecimal amount,
        LocalDate reportedAt,
        String propertyType,
        DamageCause cause,
        boolean emergencyResponseRequired
) implements Claim {

    public HomeClaim {
        if (amount.signum() < 0) {
            throw new IllegalArgumentException("amount darf nicht negativ sein");
        }
    }

    @Override
    public ClaimType type() {
        return ClaimType.HOME;
    }

    public enum DamageCause {
        WATER,
        FIRE,
        STORM,
        THEFT
    }
}
