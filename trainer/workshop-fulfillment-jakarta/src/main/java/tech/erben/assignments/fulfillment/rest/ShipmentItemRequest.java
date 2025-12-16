package tech.erben.assignments.fulfillment.rest;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ShipmentItemRequest {

    @NotNull
    private Long lineItemId;

    @NotNull
    @Min(1)
    private Integer quantity;

    public Long getLineItemId() {
        return lineItemId;
    }

    public void setLineItemId(Long lineItemId) {
        this.lineItemId = lineItemId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
