package tech.erben.assignments.fulfillment.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import tech.erben.assignments.fulfillment.domain.FulfillmentOrder;
import tech.erben.assignments.fulfillment.domain.OrderLineItem;
import tech.erben.assignments.fulfillment.domain.OrderStatus;

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
}
