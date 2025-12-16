package tech.erben.java17.records.model;

import java.time.LocalDateTime;

public class TestDriveBooking {

    private Customer customer;
    private Car car;
    private LocalDateTime slot;
    private String status;

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

    public LocalDateTime getSlot() {
        return slot;
    }

    public void setSlot(LocalDateTime slot) {
        this.slot = slot;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void reschedule(LocalDateTime newSlot) {
        this.slot = newSlot;
        this.status = "RESCHEDULED";
    }
}
