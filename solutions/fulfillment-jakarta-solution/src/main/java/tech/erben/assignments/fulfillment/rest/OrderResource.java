package tech.erben.assignments.fulfillment.rest;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import tech.erben.assignments.fulfillment.domain.FulfillmentOrder;
import tech.erben.assignments.fulfillment.domain.OrderLineItem;
import tech.erben.assignments.fulfillment.domain.projection.OrderDetail;
import tech.erben.assignments.fulfillment.domain.projection.OrderSummary;
import tech.erben.assignments.fulfillment.service.OrderService;

import java.net.URI;
import java.util.List;

@Path("/orders")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource {

    @Inject
    OrderService orderService;

    @GET
    public List<OrderSummary> getAll() {
        return orderService.listOrderSummaries();
    }

    @GET
    @Path("{id}")
    public OrderDetail byId(@PathParam("id") Long id) {
        return orderService.getOrderView(id);
    }

    @POST
    public Response create(@Valid CreateOrderRequest request, @Context UriInfo uriInfo) {
        FulfillmentOrder order = toEntity(request);
        FulfillmentOrder saved = orderService.create(order);
        URI uri = uriInfo.getAbsolutePathBuilder().path(saved.getId().toString()).build();
        OrderDetail created = orderService.toOrderDetail(saved);
        return Response.created(uri).entity(created).build();
    }

    @PUT
    @Path("{id}/status")
    public OrderDetail updateStatus(@PathParam("id") Long id,
                                    @Valid UpdateOrderStatusRequest request) {
        orderService.updateStatus(id, request.status());
        return orderService.getOrderView(id);
    }

    private FulfillmentOrder toEntity(CreateOrderRequest request) {
        FulfillmentOrder order = new FulfillmentOrder();
        order.setOrderNumber(request.orderNumber());
        order.setCustomerReference(request.customerReference());
        order.setShippingAddress(request.shippingAddress());
        request.lineItems().forEach(itemRequest -> {
            OrderLineItem lineItem = new OrderLineItem();
            lineItem.setSku(itemRequest.sku());
            lineItem.setDescription(itemRequest.description());
            lineItem.setOrderedQuantity(itemRequest.quantity());
            order.addLineItem(lineItem);
        });
        return order;
    }
}
