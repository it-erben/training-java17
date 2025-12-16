package tech.erben.assignments.fulfillment.rest;

import java.time.OffsetDateTime;

public record ShipmentHandoverRequest(OffsetDateTime handoverAt) {
}
