package tech.erben.java17.sealedclasses.model;

import java.math.BigDecimal;

public sealed interface Claim permits CarClaim, HealthClaim, TravelClaim {

    String policyNumber();

    BigDecimal amount();
}
