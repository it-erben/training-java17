package tech.erben.assignments.fulfillment.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.erben.assignments.fulfillment.domain.Address;

import java.util.ArrayList;
import java.util.List;

public class CreateOrderRequest {

    @NotBlank
    private String orderNumber;

    private String customerReference;

    @Valid
    @NotNull
    private Address shippingAddress;

    @Valid
    @NotEmpty
    private List<LineItemRequest> lineItems = new ArrayList<>();

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCustomerReference() {
        return customerReference;
    }

    public void setCustomerReference(String customerReference) {
        this.customerReference = customerReference;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public List<LineItemRequest> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItemRequest> lineItems) {
        this.lineItems = lineItems;
    }

    public static class LineItemRequest {
        @NotBlank
        private String sku;

        private String description;

        @NotNull
        @Min(1)
        private Integer quantity;

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }
}
