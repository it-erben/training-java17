package tech.erben.java17.sealedclasses.solutions.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MotorClaim(
        String policyNumber,
        BigDecimal amount,
        LocalDate reportedAt,
        DamageSeverity severity,
        boolean drivable,
        String damageDescription
) implements Claim {

    public MotorClaim {
        if (amount.signum() < 0) {
            throw new IllegalArgumentException("amount darf nicht negativ sein");
        }
    }

    @Override
    public ClaimType type() {
        return ClaimType.MOTOR;
    }

    public enum DamageSeverity {
        MINOR,
        MAJOR
    }
}
