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
    public List<FulfillmentOrder> getAll() {
        return orderService.listOrders();
    }

    @GET
    @Path("{id}")
    public FulfillmentOrder byId(@PathParam("id") Long id) {
        return orderService.getOrder(id);
    }

    @POST
    public Response create(@Valid CreateOrderRequest request, @Context UriInfo uriInfo) {
        FulfillmentOrder order = toEntity(request);
        FulfillmentOrder saved = orderService.create(order);
        URI uri = uriInfo.getAbsolutePathBuilder().path(saved.getId().toString()).build();
        return Response.created(uri).entity(saved).build();
    }

    @PUT
    @Path("{id}/status")
    public FulfillmentOrder updateStatus(@PathParam("id") Long id,
                                         @Valid UpdateOrderStatusRequest request) {
        return orderService.updateStatus(id, request.getStatus());
    }

    private FulfillmentOrder toEntity(CreateOrderRequest request) {
        FulfillmentOrder order = new FulfillmentOrder();
        order.setOrderNumber(request.getOrderNumber());
        order.setCustomerReference(request.getCustomerReference());
        order.setShippingAddress(request.getShippingAddress());
        request.getLineItems().forEach(itemRequest -> {
            OrderLineItem lineItem = new OrderLineItem();
            lineItem.setSku(itemRequest.getSku());
            lineItem.setDescription(itemRequest.getDescription());
            lineItem.setOrderedQuantity(itemRequest.getQuantity());
            order.addLineItem(lineItem);
        });
        return order;
    }
}
