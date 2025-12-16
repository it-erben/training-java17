package tech.erben.assignments.fulfillment.domain;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "shipment_items")
public class ShipmentItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonbTransient
    @ManyToOne
    private Shipment shipment;

    @JsonbTransient
    @ManyToOne
    private OrderLineItem lineItem;

    @NotNull
    @Min(1)
    private Integer quantity;

    public Long getId() {
        return id;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public OrderLineItem getLineItem() {
        return lineItem;
    }

    public void setLineItem(OrderLineItem lineItem) {
        this.lineItem = lineItem;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Transient
    public Long getLineItemId() {
        return lineItem != null ? lineItem.getId() : null;
    }

    @Transient
    public String getSku() {
        return lineItem != null ? lineItem.getSku() : null;
    }
}
