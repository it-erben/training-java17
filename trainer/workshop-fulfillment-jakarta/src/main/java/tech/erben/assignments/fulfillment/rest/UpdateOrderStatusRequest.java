package tech.erben.assignments.fulfillment.rest;

import jakarta.validation.constraints.NotNull;
import tech.erben.assignments.fulfillment.domain.OrderStatus;

public class UpdateOrderStatusRequest {
    @NotNull
    private OrderStatus status;

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
