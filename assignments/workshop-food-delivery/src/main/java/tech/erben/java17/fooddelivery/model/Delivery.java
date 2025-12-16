package tech.erben.java17.fooddelivery.model;

import java.time.Duration;
import java.util.List;

public record Delivery(
        String id,
        Order order,
        Courier courier,
        DeliveryStatus status,
        double distanceKm,
        double progress,
        Duration estimatedArrival,
        List<DeliveryEvent> timeline
) {
}
