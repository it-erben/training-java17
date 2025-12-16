package tech.erben.assignments.fulfillment.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateShipmentRequest(
    @NotNull Long orderId,
    String carrier,
    String trackingNumber,
    @Valid @NotEmpty List<ShipmentItemRequest> items
) {
    public CreateShipmentRequest {
        items = items != null ? List.copyOf(items) : List.of();
    }
}
