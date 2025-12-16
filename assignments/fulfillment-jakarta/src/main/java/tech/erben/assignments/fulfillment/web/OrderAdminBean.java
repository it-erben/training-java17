package tech.erben.assignments.fulfillment.web;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import tech.erben.assignments.fulfillment.domain.Address;
import tech.erben.assignments.fulfillment.domain.FulfillmentOrder;
import tech.erben.assignments.fulfillment.domain.OrderLineItem;
import tech.erben.assignments.fulfillment.service.OrderService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Named
@ViewScoped
public class OrderAdminBean implements Serializable {

    private final List<LineItemForm> lineItems = new ArrayList<>();
    private List<FulfillmentOrder> orders;

    private FulfillmentOrder newOrder;
    private Address newAddress;
    private LineItemForm lineItemForm = new LineItemForm();

    @Inject
    transient OrderService orderService;

    @PostConstruct
    public void init() {
        reloadOrders();
        resetForm();
    }

    public void addLineItem() {
        if (lineItemForm.getSku() == null || lineItemForm.getSku().isBlank() || lineItemForm.getQuantity() == null) {
            return;
        }
        lineItems.add(lineItemForm);
        lineItemForm = new LineItemForm();
    }

    public void removeLineItem(LineItemForm form) {
        lineItems.remove(form);
    }

    public void createOrder() {
        try {
            if (lineItems.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Add at least one line item before creating the order", null));
                return;
            }
            FulfillmentOrder order = new FulfillmentOrder();
            order.setOrderNumber(newOrder.getOrderNumber());
            order.setCustomerReference(newOrder.getCustomerReference());
            order.setShippingAddress(newAddress);

            lineItems.forEach(item -> {
                OrderLineItem li = new OrderLineItem();
                li.setSku(item.getSku());
                li.setDescription(item.getDescription());
                li.setOrderedQuantity(item.getQuantity());
                order.addLineItem(li);
            });

            orderService.create(order);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Order created", order.getOrderNumber()));
            reloadOrders();
            resetForm();
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Could not create order: " + ex.getMessage(), null));
        }
    }

    public void populateRandom() {
        resetForm();
        ThreadLocalRandom r = ThreadLocalRandom.current();
        newOrder.setOrderNumber("WEB-" + (r.nextInt(1000, 9999)));
        newOrder.setCustomerReference("Random customer " + (char) ('A' + r.nextInt(0, 26)));

        newAddress.setLine1(r.nextInt(1, 99) + " Warehouse Street");
        newAddress.setCity("City " + r.nextInt(1, 20));
        newAddress.setStateOrProvince("ST");
        newAddress.setPostalCode(String.valueOf(r.nextInt(10000, 99999)));
        newAddress.setCountry("DE");

        addRandomLineItem("SKU-" + r.nextInt(100, 999), "Sample item A", r.nextInt(1, 5));
        addRandomLineItem("SKU-" + r.nextInt(100, 999), "Sample item B", r.nextInt(1, 3));
    }

    private void addRandomLineItem(String sku, String desc, int qty) {
        LineItemForm li = new LineItemForm();
        li.setSku(sku);
        li.setDescription(desc);
        li.setQuantity(qty);
        lineItems.add(li);
    }

    private void reloadOrders() {
        orders = orderService.listOrders();
    }

    private void resetForm() {
        newOrder = new FulfillmentOrder();
        newAddress = new Address();
        lineItems.clear();
        lineItemForm = new LineItemForm();
    }

    public List<FulfillmentOrder> getOrders() {
        return orders;
    }

    public FulfillmentOrder getNewOrder() {
        return newOrder;
    }

    public Address getNewAddress() {
        return newAddress;
    }

    public LineItemForm getLineItemForm() {
        return lineItemForm;
    }

    public List<LineItemForm> getLineItems() {
        return lineItems;
    }

    public static class LineItemForm implements Serializable {
        private String sku;
        private String description;
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
