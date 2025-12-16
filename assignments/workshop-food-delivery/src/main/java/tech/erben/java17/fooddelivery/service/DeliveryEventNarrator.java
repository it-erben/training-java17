package tech.erben.java17.fooddelivery.service;

import tech.erben.java17.fooddelivery.model.DeliveryEvent;
import org.springframework.stereotype.Service;

@Service
public class DeliveryEventNarrator {

    public String describe(DeliveryEvent event) {
        if (event instanceof DeliveryEvent.CourierAssigned assigned) {
            return "Kurier %s wurde mit %s zugeteilt.".formatted(assigned.courierName(), assigned.vehicleType());
        } else if (event instanceof DeliveryEvent.PackagePrepared prepared) {
            return "Bestellung fertig in Station %s.".formatted(prepared.kitchenStation());
        } else if (event instanceof DeliveryEvent.PackagePickedUp picked) {
            return "Abholung erfolgt am %s.".formatted(picked.pickupLocation());
        } else if (event instanceof DeliveryEvent.DeliveryDelayed delayed) {
            return "Verspätung (%s): +%d Minuten.".formatted(delayed.reason(), delayed.additionalMinutes());
        } else if (event instanceof DeliveryEvent.CustomerUnavailable unavailable) {
            return "Kund:in nicht erreichbar – %s.".formatted(unavailable.note());
        } else if (event instanceof DeliveryEvent.Delivered delivered) {
            return "Erfolgreich zugestellt (%s).".formatted(delivered.dropOffNotes());
        }
        return "Unbekanntes Ereignis";
    }
}
