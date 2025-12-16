package tech.erben.assignments.fulfillment.domain.projection;

import tech.erben.assignments.fulfillment.domain.ShipmentStatus;

import java.time.OffsetDateTime;
import java.util.List;

public record ShipmentDetail(
    Long id,
    ShipmentStatus status,
    String carrier,
    String trackingNumber,
    OffsetDateTime handoverAt,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt,
    List<ShipmentItemView> items
) implements ShipmentView {

    public ShipmentDetail {
        items = items != null ? List.copyOf(items) : List.of();
    }
}
