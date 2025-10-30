package tech.erben.java17.records.solution.model;

import java.time.LocalDateTime;
import java.util.Objects;

public record TestDriveBooking(
        Customer customer,
        Car car,
        LocalDateTime slot,
        BookingStatus status
) {

    public TestDriveBooking {
        Objects.requireNonNull(customer, "customer darf nicht null sein");
        Objects.requireNonNull(car, "car darf nicht null sein");
        Objects.requireNonNull(slot, "slot darf nicht null sein");
        Objects.requireNonNull(status, "status darf nicht null sein");
    }

    public TestDriveBooking reschedule(LocalDateTime newSlot) {
        Objects.requireNonNull(newSlot, "newSlot darf nicht null sein");
        return new TestDriveBooking(customer, car, newSlot, BookingStatus.RESCHEDULED);
    }

    public TestDriveBooking updateStatus(BookingStatus newStatus) {
        Objects.requireNonNull(newStatus, "newStatus darf nicht null sein");
        return new TestDriveBooking(customer, car, slot, newStatus);
    }

    public enum BookingStatus {
        CONFIRMED,
        PENDING,
        WAITING_LIST,
        RESCHEDULED,
        REVIEW_PRICE_DROP
    }
}
