package tech.erben.assignments.fulfillment.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CreateShipmentRequest {

    @NotNull
    private Long orderId;

    private String carrier;

    private String trackingNumber;

    @Valid
    @NotEmpty
    private List<ShipmentItemRequest> items = new ArrayList<>();

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public List<ShipmentItemRequest> getItems() {
        return items;
    }

    public void setItems(List<ShipmentItemRequest> items) {
        this.items = items;
    }
}
