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
import tech.erben.assignments.fulfillment.domain.projection.ShipmentDetail;
import tech.erben.assignments.fulfillment.domain.projection.ShipmentSummary;
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
    public List<ShipmentSummary> all() {
        return shipmentService.listShipmentSummaries();
    }

    @GET
    @Path("{id}")
    public ShipmentDetail byId(@PathParam("id") Long id) {
        return shipmentService.getShipmentView(id);
    }

    @POST
    public Response create(@Valid CreateShipmentRequest request, @Context UriInfo uriInfo) {
        FulfillmentOrder order = orderService.getOrder(request.orderId());
        List<ShipmentItem> items = request.items().stream()
            .map(itemRequest -> toShipmentItem(order, itemRequest))
            .toList();
        Shipment shipment = shipmentService.createShipment(order, request.carrier(), request.trackingNumber(), items);
        URI uri = uriInfo.getAbsolutePathBuilder().path(shipment.getId().toString()).build();
        ShipmentDetail detail = shipmentService.toShipmentDetail(shipment);
        return Response.created(uri).entity(detail).build();
    }

    @PUT
    @Path("{id}/handover")
    public ShipmentDetail markInTransit(@PathParam("id") Long id,
                                        @Valid ShipmentHandoverRequest request) {
        OffsetDateTime handoverAt = request != null ? request.handoverAt() : null;
        Shipment shipment = shipmentService.markInTransit(id, handoverAt);
        return shipmentService.toShipmentDetail(shipment);
    }

    private ShipmentItem toShipmentItem(FulfillmentOrder order, ShipmentItemRequest request) {
        OrderLineItem lineItem = orderService.getLineItem(request.lineItemId());
        if (!order.getId().equals(lineItem.getOrder().getId())) {
            throw new BadRequestException("Line item " + request.lineItemId() + " does not belong to order " + order.getId());
        }
        ShipmentItem item = new ShipmentItem();
        item.setLineItem(lineItem);
        item.setQuantity(request.quantity());
        return item;
    }
}
