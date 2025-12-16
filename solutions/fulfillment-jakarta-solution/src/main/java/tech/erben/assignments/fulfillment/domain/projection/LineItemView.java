package tech.erben.assignments.fulfillment.domain.projection;

public record LineItemView(
    Long id,
    String sku,
    String description,
    Integer orderedQuantity,
    Integer allocatedQuantity,
    Integer pickedQuantity
) {
}
