package tech.erben.java17.records.solution.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record LeasingOffer(
        String offerId,
        BigDecimal downPayment,
        int durationMonths,
        BigDecimal monthlyRate,
        Customer customer,
        Car car
) {

    public LeasingOffer {
        Objects.requireNonNull(offerId, "offerId darf nicht null sein");
        Objects.requireNonNull(downPayment, "downPayment darf nicht null sein");
        if (durationMonths <= 0) {
            throw new IllegalArgumentException("durationMonths muss größer 0 sein");
        }
        Objects.requireNonNull(monthlyRate, "monthlyRate darf nicht null sein");
        Objects.requireNonNull(customer, "customer darf nicht null sein");
        Objects.requireNonNull(car, "car darf nicht null sein");
        downPayment = downPayment.setScale(2, RoundingMode.HALF_UP);
        monthlyRate = monthlyRate.setScale(2, RoundingMode.HALF_UP);
    }

    public LeasingOffer recalculateForPrice(BigDecimal vehiclePrice) {
        Objects.requireNonNull(vehiclePrice, "vehiclePrice darf nicht null sein");
        var basis = vehiclePrice.subtract(downPayment);
        if (basis.signum() < 0) {
            basis = BigDecimal.ZERO;
        }
        var newRate = basis.divide(BigDecimal.valueOf(durationMonths), 2, RoundingMode.HALF_UP);
        return new LeasingOffer(offerId, downPayment, durationMonths, newRate, customer, car);
    }

    public LeasingOffer adjustRate(BigDecimal delta) {
        Objects.requireNonNull(delta, "delta darf nicht null sein");
        return new LeasingOffer(offerId, downPayment, durationMonths, monthlyRate.add(delta), customer, car);
    }
}
