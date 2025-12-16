package tech.erben.assignments.fulfillment.rest;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.BadRequestException;
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
import tech.erben.assignments.fulfillment.domain.Shipment;
import tech.erben.assignments.fulfillment.domain.ShipmentItem;
import tech.erben.assignments.fulfillment.service.OrderService;
import tech.erben.assignments.fulfillment.service.ShipmentService;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

@Path("/shipments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ShipmentResource {

    @Inject
    ShipmentService shipmentService;

    @Inject
    OrderService orderService;

    @GET
    public List<Shipment> all() {
        return shipmentService.listShipments();
    }

    @GET
    @Path("{id}")
    public Shipment byId(@PathParam("id") Long id) {
        return shipmentService.getShipment(id);
    }

    @POST
    public Response create(@Valid CreateShipmentRequest request, @Context UriInfo uriInfo) {
        FulfillmentOrder order = orderService.getOrder(request.getOrderId());
        List<ShipmentItem> items = request.getItems().stream()
            .map(itemRequest -> toShipmentItem(order, itemRequest))
            .toList();
        Shipment shipment = shipmentService.createShipment(order, request.getCarrier(), request.getTrackingNumber(), items);
        URI uri = uriInfo.getAbsolutePathBuilder().path(shipment.getId().toString()).build();
        return Response.created(uri).entity(shipment).build();
    }

    @PUT
    @Path("{id}/handover")
    public Shipment markInTransit(@PathParam("id") Long id,
                                  @Valid ShipmentHandoverRequest request) {
        OffsetDateTime handoverAt = request != null ? request.getHandoverAt() : null;
        return shipmentService.markInTransit(id, handoverAt);
    }

    private ShipmentItem toShipmentItem(FulfillmentOrder order, ShipmentItemRequest request) {
        OrderLineItem lineItem = orderService.getLineItem(request.getLineItemId());
        if (!order.getId().equals(lineItem.getOrder().getId())) {
            throw new BadRequestException("Line item " + request.getLineItemId() + " does not belong to order " + order.getId());
        }
        ShipmentItem item = new ShipmentItem();
        item.setLineItem(lineItem);
        item.setQuantity(request.getQuantity());
        return item;
    }
}
