package tech.erben.assignments.fulfillment.rest;

import jakarta.validation.constraints.NotNull;
import tech.erben.assignments.fulfillment.domain.OrderStatus;

public record UpdateOrderStatusRequest(@NotNull OrderStatus status) {
}
