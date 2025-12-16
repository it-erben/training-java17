package tech.erben.assignments.fulfillment.domain.projection;

import tech.erben.assignments.fulfillment.domain.OrderStatus;

public sealed interface OrderView permits OrderSummary, OrderDetail {
    Long id();

    String orderNumber();

    OrderStatus status();
}
