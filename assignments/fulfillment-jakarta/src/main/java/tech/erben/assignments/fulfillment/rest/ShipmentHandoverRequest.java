package tech.erben.assignments.fulfillment.rest;

import java.time.OffsetDateTime;

public class ShipmentHandoverRequest {
    private OffsetDateTime handoverAt;

    public OffsetDateTime getHandoverAt() {
        return handoverAt;
    }

    public void setHandoverAt(OffsetDateTime handoverAt) {
        this.handoverAt = handoverAt;
    }
}
