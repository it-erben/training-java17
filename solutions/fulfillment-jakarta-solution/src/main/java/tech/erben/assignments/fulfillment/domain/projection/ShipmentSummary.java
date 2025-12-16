package tech.erben.assignments.fulfillment.domain.projection;

import tech.erben.assignments.fulfillment.domain.ShipmentStatus;

import java.time.OffsetDateTime;

public record ShipmentSummary(
    Long id,
    ShipmentStatus status,
    String carrier,
    String trackingNumber,
    OffsetDateTime handoverAt,
    OffsetDateTime createdAt
) implements ShipmentView {
}
