package tech.erben.assignments.fulfillment.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import tech.erben.assignments.fulfillment.domain.FulfillmentOrder;
import tech.erben.assignments.fulfillment.domain.OrderLineItem;
import tech.erben.assignments.fulfillment.domain.OrderStatus;
import tech.erben.assignments.fulfillment.domain.Shipment;
import tech.erben.assignments.fulfillment.domain.projection.LineItemView;
import tech.erben.assignments.fulfillment.domain.projection.OrderDetail;
import tech.erben.assignments.fulfillment.domain.projection.OrderSummary;
import tech.erben.assignments.fulfillment.domain.projection.ShipmentDetail;
import tech.erben.assignments.fulfillment.domain.projection.ShipmentItemView;
import tech.erben.assignments.fulfillment.domain.projection.ShipmentView;

import java.util.List;

@ApplicationScoped
public class OrderService {

    @PersistenceContext(unitName = "fulfillmentPU")
    EntityManager entityManager;

    public List<FulfillmentOrder> listOrders() {
        return entityManager.createQuery("""
                select distinct o from FulfillmentOrder o
                left join fetch o.lineItems
                order by o.createdAt desc
                """, FulfillmentOrder.class)
            .getResultList();
    }

    public List<OrderSummary> listOrderSummaries() {
        return entityManager.createQuery("""
                select new tech.erben.assignments.fulfillment.domain.projection.OrderSummary(
                    o.id,
                    o.orderNumber,
                    o.customerReference,
                    o.status,
                    o.shippingAddress,
                    o.createdAt
                )
                from FulfillmentOrder o
                order by o.createdAt desc
                """, OrderSummary.class)
            .getResultList();
    }

    public long countOrders() {
        return entityManager.createQuery("select count(o) from FulfillmentOrder o", Long.class)
            .getSingleResult();
    }

    public FulfillmentOrder getOrder(Long id) {
        return entityManager.createQuery("""
                        select distinct o from FulfillmentOrder o
                        left join fetch o.lineItems
                        where o.id = :id
                """, FulfillmentOrder.class)
            .setParameter("id", id)
            .getResultStream()
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Order " + id + " not found"));
    }

    public OrderDetail getOrderView(Long id) {
        FulfillmentOrder order = entityManager.createQuery("""
                        select distinct o from FulfillmentOrder o
                        left join fetch o.lineItems oli
                        where o.id = :id
                """, FulfillmentOrder.class)
            .setParameter("id", id)
            .getResultStream()
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Order " + id + " not found"));
        List<Shipment> shipments = entityManager.createQuery("""
                        select distinct s from Shipment s
                        left join fetch s.items si
                        left join fetch si.lineItem sli
                        where s.order.id = :orderId
                        order by s.createdAt desc
                """, Shipment.class)
            .setParameter("orderId", id)
            .getResultList();
        return toOrderDetail(order, shipments);
    }

    @Transactional
    public FulfillmentOrder create(FulfillmentOrder order) {
        order.getLineItems().forEach(item -> item.setOrder(order));
        entityManager.persist(order);
        return order;
    }

    @Transactional
    public FulfillmentOrder updateStatus(Long id, OrderStatus status) {
        FulfillmentOrder order = entityManager.find(FulfillmentOrder.class, id);
        if (order == null) {
            throw new NotFoundException("Order " + id + " not found");
        }
        order.setStatus(status);
        return order;
    }

    public OrderLineItem getLineItem(Long id) {
        OrderLineItem item = entityManager.find(OrderLineItem.class, id);
        if (item == null) {
            throw new NotFoundException("Line item " + id + " not found");
        }
        return item;
    }

    public OrderDetail toOrderDetail(FulfillmentOrder order) {
        return toOrderDetail(order, order.getShipments());
    }

    public OrderDetail toOrderDetail(FulfillmentOrder order, List<Shipment> shipments) {
        List<LineItemView> lineItems = order.getLineItems().stream()
            .map(item -> new LineItemView(
                item.getId(),
                item.getSku(),
                item.getDescription(),
                item.getOrderedQuantity(),
                item.getAllocatedQuantity(),
                item.getPickedQuantity()
            ))
            .toList();

        List<ShipmentView> shipmentViews = shipments.stream()
            .map(this::toShipmentDetail)
            .map(ShipmentView.class::cast)
            .toList();

        return new OrderDetail(
            order.getId(),
            order.getOrderNumber(),
            order.getCustomerReference(),
            order.getStatus(),
            order.getShippingAddress(),
            lineItems,
            shipmentViews,
            order.getCreatedAt(),
            order.getUpdatedAt()
        );
    }

    private ShipmentDetail toShipmentDetail(Shipment shipment) {
        List<ShipmentItemView> items = shipment.getItems().stream()
            .map(item -> new ShipmentItemView(item.getId(), item.getLineItemId(), item.getSku(), item.getQuantity()))
            .toList();
        return new ShipmentDetail(
            shipment.getId(),
            shipment.getStatus(),
            shipment.getCarrier(),
            shipment.getTrackingNumber(),
            shipment.getHandoverAt(),
            shipment.getCreatedAt(),
            shipment.getUpdatedAt(),
            items
        );
    }
}
