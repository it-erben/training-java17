package tech.erben.java17.fooddelivery.solution.service;

import tech.erben.java17.fooddelivery.solution.model.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class DeliveryBoardService {

    private final DeliveryEventNarrator narrator;

    public DeliveryBoardService(DeliveryEventNarrator narrator) {
        this.narrator = narrator;
    }

    public List<Delivery> deliveries() {
        var burgerDistrict = new Restaurant("Burger District", "Mitte", true);
        var veggieGarden = new Restaurant("Veggie Garden", "Prenzlauer Berg", false);
        var ramenExpress = new Restaurant("Ramen Express", "Friedrichshain", true);
        var tandooriPlace = new Restaurant("Tandoori Place", "Neukölln", true);
        var sushiWorks = new Restaurant("Sushi Works", "Charlottenburg", false);

        var anna = new Customer("Anna Reuter", "+49 30 123456", "Gold");
        var jonah = new Customer("Jonah Klein", "+49 30 223344", "Platin");
        var marta = new Customer("Marta Li", "+49 30 998811", "Silber");
        var theo = new Customer("Theo Braun", "+49 151 55555", "Bronze");
        var fatima = new Customer("Fatima Saleh", "+49 176 111222", "Gold");

        var courierIris = new Courier("Iris", "Bike", true, 4.9);
        var courierLeo = new Courier("Leo", "Car", true, 4.7);
        var courierNora = new Courier("Nora", "Scooter", true, 4.8);
        var courierKai = new Courier("Kai", "E-Bike", false, 4.6);
        var courierMila = new Courier("Mila", "Foot", true, 4.5);

        var now = LocalDateTime.now();

        return List.of(
                new Delivery(
                        "FD-1001",
                        new Order("ORD-1", anna, burgerDistrict, List.of("Smoked Burger", "Sweet Potato Fries"), 21.40,
                                now.minusMinutes(25)),
                        courierIris,
                        DeliveryStatus.ON_THE_WAY,
                        4.2,
                        0.65,
                        Duration.ofMinutes(12),
                        List.of(
                                new DeliveryEvent.CourierAssigned(now.minusMinutes(20), courierIris.name(), courierIris.vehicleType()),
                                new DeliveryEvent.PackagePrepared(now.minusMinutes(15), "Grillstation"),
                                new DeliveryEvent.PackagePickedUp(now.minusMinutes(10), "Haupteingang"))
                ),
                new Delivery(
                        "FD-1002",
                        new Order("ORD-2", jonah, veggieGarden, List.of("Tempeh Bowl", "Matcha Latte"), 18.90,
                                now.minusMinutes(40)),
                        courierLeo,
                        DeliveryStatus.DELAYED,
                        7.8,
                        0.45,
                        Duration.ofMinutes(25),
                        List.of(
                                new DeliveryEvent.CourierAssigned(now.minusMinutes(35), courierLeo.name(), courierLeo.vehicleType()),
                                new DeliveryEvent.PackagePrepared(now.minusMinutes(30), "Veggie-Line"),
                                new DeliveryEvent.DeliveryDelayed(now.minusMinutes(5), "Stau auf Leipziger Straße", 12),
                                new DeliveryEvent.PaymentIssue(now.minusMinutes(3), "Stripe", "Autorisation erneut"))
                ),
                new Delivery(
                        "FD-1003",
                        new Order("ORD-3", marta, ramenExpress, List.of("Miso Ramen", "Gyoza"), 24.50,
                                now.minusMinutes(10)),
                        courierNora,
                        DeliveryStatus.PREPARING,
                        2.1,
                        0.15,
                        Duration.ofMinutes(32),
                        List.of(
                                new DeliveryEvent.PackagePrepared(now.minusMinutes(2), "Suppenküche"))
                ),
                new Delivery(
                        "FD-1004",
                        new Order("ORD-4", theo, tandooriPlace, List.of("Chicken Tikka", "Garlic Naan"), 27.30,
                                now.minusMinutes(55)),
                        courierKai,
                        DeliveryStatus.CANCELLED,
                        5.5,
                        0.20,
                        Duration.ofMinutes(0),
                        List.of(
                                new DeliveryEvent.CourierAssigned(now.minusMinutes(45), courierKai.name(), courierKai.vehicleType()),
                                new DeliveryEvent.CustomerUnavailable(now.minusMinutes(5), "Keine Antwort an der Haustür"))
                ),
                new Delivery(
                        "FD-1005",
                        new Order("ORD-5", fatima, sushiWorks, List.of("Rainbow Roll", "Edamame"), 19.80,
                                now.minusMinutes(70)),
                        courierMila,
                        DeliveryStatus.DELIVERED,
                        3.3,
                        1.0,
                        Duration.ZERO,
                        List.of(
                                new DeliveryEvent.CourierAssigned(now.minusMinutes(60), courierMila.name(), courierMila.vehicleType()),
                                new DeliveryEvent.PackagePrepared(now.minusMinutes(55), "Sushi-Bar"),
                                new DeliveryEvent.PackagePickedUp(now.minusMinutes(50), "Nebeneingang"),
                                new DeliveryEvent.Delivered(now.minusMinutes(5), "Bei Nachbarin abgegeben"))
                )
        );
    }

    public Map<DeliveryStatus, Long> statusSummary(List<Delivery> deliveries) {
        var summary = new EnumMap<DeliveryStatus, Long>(DeliveryStatus.class);
        for (Delivery delivery : deliveries) {
            summary.merge(delivery.status(), 1L, Long::sum);
        }
        return summary;
    }

    public String statusBadge(DeliveryStatus status) {
        return switch (status) {
            case CREATED, PREPARING -> "badge-open";
            case READY_FOR_PICKUP, ON_THE_WAY -> "badge-active";
            case DELIVERED -> "badge-success";
            case DELAYED -> "badge-warning";
            case CANCELLED -> "badge-cancelled";
        };
    }

    public String etaMessage(Delivery delivery) {
        return switch (delivery.status()) {
            case CREATED -> "Bestellung eingegangen, Küche plant.";
            case PREPARING -> "Im Küchenprozess – geschätzte Lieferung in %d Minuten.".formatted(delivery.estimatedArrival().toMinutes());
            case READY_FOR_PICKUP -> "Abholung steht bevor – bitte Klingel bereithalten.";
            case ON_THE_WAY -> "Unterwegs! Noch %.1f km".formatted(delivery.distanceKm());
            case DELIVERED -> "Abgeschlossen – guten Appetit!";
            case DELAYED -> "Verspätet (%.0f%% fertig) – bitte Geduld.".formatted(delivery.progress() * 100);
            case CANCELLED -> "Storniert – Support informiert.";
        };
    }

    public String vehicleEmoji(Courier courier) {
        return switch (courier.vehicleType().toLowerCase()) {
            case "bike", "e-bike" -> "🚴";
            case "scooter" -> "🛵";
            case "car" -> "🚗";
            case "foot" -> "🚶";
            default -> "🚚";
        };
    }

    public String highlightClass(Delivery delivery) {
        var highValue = delivery.order().totalAmount() >= 25;
        var nearCompletion = delivery.progress() >= 0.8;
        if (delivery.status() == DeliveryStatus.DELAYED) {
            return "card-delayed";
        }
        if (delivery.status() == DeliveryStatus.CANCELLED) {
            return "card-cancelled";
        }
        if (highValue && nearCompletion) {
            return "card-vip";
        }
        return "";
    }

    public String zoneByDistance(double distanceKm) {
        return switch ((int) Math.floor(distanceKm)) {
            case 0, 1 -> "Zone A";
            case 2, 3 -> "Zone B";
            case 4, 5 -> "Zone C";
            case 6, 7 -> "Zone D";
            default -> "Zone E";
        };
    }

    public List<Delivery> sortedByEta(List<Delivery> deliveries) {
        return deliveries.stream()
                .sorted(Comparator.comparing(Delivery::estimatedArrival))
                .toList();
    }

    public String narrative(DeliveryEvent event) {
        return narrator.describe(event);
    }
}
