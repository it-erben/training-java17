package tech.erben.java17.sealedclasses.solutions.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TravelClaim(
        String policyNumber,
        BigDecimal amount,
        LocalDate reportedAt,
        String destinationCountry,
        IncidentType incidentType,
        boolean documentationComplete
) implements Claim {

    public TravelClaim {
        if (amount.signum() < 0) {
            throw new IllegalArgumentException("amount darf nicht negativ sein");
        }
    }

    @Override
    public ClaimType type() {
        return ClaimType.TRAVEL;
    }

    public enum IncidentType {
        DELAY,
        MEDICAL,
        LOSS
    }
}
