package tech.erben.assignments.fulfillment.domain.projection;

public record ShipmentItemView(
    Long id,
    Long lineItemId,
    String sku,
    Integer quantity
) {
}
