package tech.erben.assignments.fulfillment.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.erben.assignments.fulfillment.domain.Address;

import java.util.List;

public record CreateOrderRequest(
    @NotBlank String orderNumber,
    String customerReference,
    @Valid @NotNull Address shippingAddress,
    @Valid @NotEmpty List<LineItemRequest> lineItems
) {
    public CreateOrderRequest {
        lineItems = lineItems != null ? List.copyOf(lineItems) : List.of();
    }

    public record LineItemRequest(
        @NotBlank String sku,
        String description,
        @NotNull @Min(1) Integer quantity
    ) {
    }
}
