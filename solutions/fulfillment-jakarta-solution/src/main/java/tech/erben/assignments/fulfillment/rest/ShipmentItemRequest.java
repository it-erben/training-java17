package tech.erben.assignments.fulfillment.rest;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ShipmentItemRequest(
    @NotNull Long lineItemId,
    @NotNull @Min(1) Integer quantity
) {
}
