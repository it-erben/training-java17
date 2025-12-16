package tech.erben.assignments.fulfillment.bootstrap;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import tech.erben.assignments.fulfillment.domain.Address;
import tech.erben.assignments.fulfillment.domain.FulfillmentOrder;
import tech.erben.assignments.fulfillment.domain.OrderLineItem;
import tech.erben.assignments.fulfillment.service.OrderService;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@ApplicationScoped
public class SampleDataLoader {

    @Inject
    OrderService orderService;

    @PostConstruct
    @Transactional
    public void init() {
        if (orderService.countOrders() > 0) {
            return;
        }
        seed("WEB-1001", "First customer", "1 Fulfillment Way", "Leipzig", "SN", "04109", "DE",
            List.of(
                line("SKU-123", "Widget", 2),
                line("SKU-456", "Gadget", 1)
            ));

        seed("WEB-1002", "VIP priority", "42 Supply Chain Ave", "Berlin", "BE", "10115", "DE",
            List.of(
                line("SKU-789", "Doohickey", 3),
                line("SKU-234", "Accessory", 4)
            ));
    }

    private void seed(String orderNumber,
                      String customerRef,
                      String street,
                      String city,
                      String state,
                      String postal,
                      String country,
                      List<OrderLineItem> lines) {
        FulfillmentOrder order = new FulfillmentOrder();
        order.setOrderNumber(orderNumber);
        order.setCustomerReference(customerRef);
        Address address = new Address(street, null, city, state, postal, country);
        order.setShippingAddress(address);
        lines.forEach(order::addLineItem);
        orderService.create(order);
    }

    private OrderLineItem line(String sku, String desc, int qty) {
        OrderLineItem item = new OrderLineItem();
        item.setSku(sku);
        item.setDescription(desc);
        item.setOrderedQuantity(qty);
        item.setAllocatedQuantity(ThreadLocalRandom.current().nextInt(0, qty + 1));
        return item;
    }
}
