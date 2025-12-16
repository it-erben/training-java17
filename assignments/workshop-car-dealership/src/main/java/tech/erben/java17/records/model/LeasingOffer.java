package tech.erben.java17.records.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class LeasingOffer {

    private String offerId;
    private BigDecimal downPayment;
    private int durationMonths;
    private BigDecimal monthlyRate;
    private Customer customer;
    private Car car;

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public BigDecimal getDownPayment() {
        return downPayment;
    }

    public void setDownPayment(BigDecimal downPayment) {
        this.downPayment = downPayment;
    }

    public int getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(int durationMonths) {
        this.durationMonths = durationMonths;
    }

    public BigDecimal getMonthlyRate() {
        return monthlyRate;
    }

    public void setMonthlyRate(BigDecimal monthlyRate) {
        this.monthlyRate = monthlyRate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public void recalculateMonthlyRate(BigDecimal vehiclePrice) {
        if (vehiclePrice == null || downPayment == null || durationMonths == 0) {
            this.monthlyRate = BigDecimal.ZERO;
            return;
        }
        var basis = vehiclePrice.subtract(downPayment);
        if (basis.compareTo(BigDecimal.ZERO) < 0) {
            basis = BigDecimal.ZERO;
        }
        this.monthlyRate = basis
                .divide(BigDecimal.valueOf(durationMonths), 2, RoundingMode.HALF_UP);
    }
}
