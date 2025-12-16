package tech.erben.java17.fooddelivery.solution.model;

import java.time.LocalDateTime;

public sealed interface DeliveryEvent permits DeliveryEvent.CourierAssigned,
        DeliveryEvent.PackagePrepared,
        DeliveryEvent.PackagePickedUp,
        DeliveryEvent.DeliveryDelayed,
        DeliveryEvent.CustomerUnavailable,
        DeliveryEvent.Delivered,
        DeliveryEvent.PaymentIssue {

    LocalDateTime occurredAt();

    record CourierAssigned(LocalDateTime occurredAt, String courierName, String vehicleType) implements DeliveryEvent {}

    record PackagePrepared(LocalDateTime occurredAt, String kitchenStation) implements DeliveryEvent {}

    record PackagePickedUp(LocalDateTime occurredAt, String pickupLocation) implements DeliveryEvent {}

    record DeliveryDelayed(LocalDateTime occurredAt, String reason, int additionalMinutes) implements DeliveryEvent {}

    record CustomerUnavailable(LocalDateTime occurredAt, String note) implements DeliveryEvent {}

    record Delivered(LocalDateTime occurredAt, String dropOffNotes) implements DeliveryEvent {}

    record PaymentIssue(LocalDateTime occurredAt, String provider, String message) implements DeliveryEvent {}
}
