package tech.erben.assignments.fulfillment.domain.projection;

import tech.erben.assignments.fulfillment.domain.Address;
import tech.erben.assignments.fulfillment.domain.OrderStatus;

import java.time.OffsetDateTime;
import java.util.List;

public record OrderDetail(
    Long id,
    String orderNumber,
    String customerReference,
    OrderStatus status,
    Address shippingAddress,
    List<LineItemView> lineItems,
    List<ShipmentView> shipments,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) implements OrderView {

    public OrderDetail {
        lineItems = lineItems != null ? List.copyOf(lineItems) : List.of();
        shipments = shipments != null ? List.copyOf(shipments) : List.of();
    }
}
