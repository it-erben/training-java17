package tech.erben.assignments.fulfillment.domain.projection;

import tech.erben.assignments.fulfillment.domain.Address;
import tech.erben.assignments.fulfillment.domain.OrderStatus;

import java.time.OffsetDateTime;

public record OrderSummary(
    Long id,
    String orderNumber,
    String customerReference,
    OrderStatus status,
    Address shippingAddress,
    OffsetDateTime createdAt
) implements OrderView {
}
