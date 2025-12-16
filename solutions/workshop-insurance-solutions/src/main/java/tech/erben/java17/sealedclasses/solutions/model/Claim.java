package tech.erben.java17.sealedclasses.solutions.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public sealed interface Claim permits MotorClaim, HealthClaim, TravelClaim, HomeClaim {
    String policyNumber();

    BigDecimal amount();

    LocalDate reportedAt();

    ClaimType type();

    enum ClaimType {
        MOTOR,
        HEALTH,
        TRAVEL,
        HOME
    }
}
