package tech.erben.assignments.fulfillment.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import tech.erben.assignments.fulfillment.domain.FulfillmentOrder;
import tech.erben.assignments.fulfillment.domain.OrderLineItem;
import tech.erben.assignments.fulfillment.domain.OrderStatus;
import tech.erben.assignments.fulfillment.domain.Shipment;
import tech.erben.assignments.fulfillment.domain.ShipmentItem;
import tech.erben.assignments.fulfillment.domain.ShipmentStatus;

import java.time.OffsetDateTime;
import java.util.List;

@ApplicationScoped
public class ShipmentService {

    @PersistenceContext(unitName = "fulfillmentPU")
    EntityManager entityManager;

    public List<Shipment> listShipments() {
        return entityManager.createQuery("""
                select distinct s from Shipment s
                left join fetch s.items i
                left join fetch i.lineItem li
                left join fetch s.order o
                order by s.createdAt desc
                """, Shipment.class)
            .getResultList();
    }

    public Shipment getShipment(Long id) {
        return entityManager.createQuery("""
                        select distinct s from Shipment s
                        left join fetch s.items i
                        left join fetch i.lineItem li
                        left join fetch s.order o
                        where s.id = :id
                """, Shipment.class)
            .setParameter("id", id)
            .getResultStream()
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Shipment " + id + " not found"));
    }

    @Transactional
    public Shipment createShipment(FulfillmentOrder order,
                                   String carrier,
                                   String trackingNumber,
                                   List<ShipmentItem> items) {
        if (items == null || items.isEmpty()) {
            throw new BadRequestException("Shipment requires at least one item");
        }

        Shipment shipment = new Shipment();
        shipment.setOrder(order);
        shipment.setCarrier(carrier);
        shipment.setTrackingNumber(trackingNumber);
        shipment.setStatus(ShipmentStatus.READY_FOR_PICKUP);

        items.forEach(item -> {
            OrderLineItem lineItem = item.getLineItem();
            if (lineItem == null) {
                throw new BadRequestException("Shipment item missing line item reference");
            }
            validateQuantity(lineItem, item.getQuantity());
            lineItem.setAllocatedQuantity(lineItem.getAllocatedQuantity() + item.getQuantity());
            shipment.addItem(item);
        });

        order.addShipment(shipment);
        order.setStatus(OrderStatus.PACKED);
        entityManager.persist(shipment);
        return shipment;
    }

    @Transactional
    public Shipment markInTransit(Long id, OffsetDateTime handoverAt) {
        Shipment shipment = getShipment(id);
        shipment.markHandover(handoverAt != null ? handoverAt : OffsetDateTime.now());
        FulfillmentOrder order = shipment.getOrder();
        order.setStatus(OrderStatus.SHIPPED);
        return shipment;
    }

    private void validateQuantity(OrderLineItem item, int requested) {
        if (requested <= 0) {
            throw new BadRequestException("Quantity must be positive for " + item.getSku());
        }
        int ordered = item.getOrderedQuantity() != null ? item.getOrderedQuantity() : 0;
        int allocated = item.getAllocatedQuantity() != null ? item.getAllocatedQuantity() : 0;
        int remaining = ordered - allocated;
        if (requested > remaining) {
            throw new BadRequestException("Not enough quantity left on line " + item.getSku());
        }
    }
}
