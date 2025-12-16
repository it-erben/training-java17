package tech.erben.java17.fooddelivery.solution.service;

import tech.erben.java17.fooddelivery.solution.model.DeliveryEvent;
import org.springframework.stereotype.Service;

@Service
public class DeliveryEventNarrator {

    public String describe(DeliveryEvent event) {
        if (event instanceof DeliveryEvent.CourierAssigned assigned) {
            return "Kurier %s (%s) seit %s zugeteilt.".formatted(
                    assigned.courierName(), assigned.vehicleType(), assigned.occurredAt().toLocalTime());
        } else if (event instanceof DeliveryEvent.PackagePrepared prepared) {
            return "Vorbereitet in Station %s um %s.".formatted(prepared.kitchenStation(), prepared.occurredAt().toLocalTime());
        } else if (event instanceof DeliveryEvent.PackagePickedUp pickedUp) {
            return "Abholung am %s um %s.".formatted(pickedUp.pickupLocation(), pickedUp.occurredAt().toLocalTime());
        } else if (event instanceof DeliveryEvent.DeliveryDelayed delayed) {
            return "Verspätung: %s (+%d min, %s)".formatted(
                    delayed.reason(), delayed.additionalMinutes(), delayed.occurredAt().toLocalTime());
        } else if (event instanceof DeliveryEvent.CustomerUnavailable unavailable) {
            return "Kund:in nicht erreicht (%s, %s).".formatted(
                    unavailable.note(), unavailable.occurredAt().toLocalTime());
        } else if (event instanceof DeliveryEvent.Delivered delivered) {
            return "Erfolgreich zugestellt (%s, %s).".formatted(
                    delivered.dropOffNotes(), delivered.occurredAt().toLocalTime());
        } else if (event instanceof DeliveryEvent.PaymentIssue issue) {
            return "Zahlungsproblem (%s): %s um %s.".formatted(
                    issue.provider(), issue.message(), issue.occurredAt().toLocalTime());
        }
        return "Unbekanntes Ereignis";
    }
}
