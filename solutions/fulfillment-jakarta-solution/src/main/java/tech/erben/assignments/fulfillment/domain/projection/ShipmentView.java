package tech.erben.assignments.fulfillment.domain.projection;

import tech.erben.assignments.fulfillment.domain.ShipmentStatus;

import java.time.OffsetDateTime;

public sealed interface ShipmentView permits ShipmentSummary, ShipmentDetail {
    Long id();

    ShipmentStatus status();

    OffsetDateTime createdAt();
}
