package tech.erben.java17.records.solution.service;

import tech.erben.java17.records.solution.model.Car;
import tech.erben.java17.records.solution.model.Customer;
import tech.erben.java17.records.solution.model.EnergyType;
import tech.erben.java17.records.solution.model.LeasingOffer;
import tech.erben.java17.records.solution.model.SalesConsultant;
import tech.erben.java17.records.solution.model.TestDriveBooking;
import tech.erben.java17.records.solution.model.TestDriveBooking.BookingStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class InventoryService {

    private final List<Car> cars;
    private final List<LeasingOffer> leasingOffers;
    private final List<TestDriveBooking> testDriveBookings;
    private final AtomicReference<Map<String, BigDecimal>> discountHistory = new AtomicReference<>(Map.of());

    public InventoryService() {
        var seed = seedData();
        this.cars = seed.cars();
        this.leasingOffers = seed.leasingOffers();
        this.testDriveBookings = seed.testDriveBookings();
    }

    public List<Car> inventory() {
        return cars;
    }

    public List<LeasingOffer> leasingOffers() {
        return leasingOffers;
    }

    public List<TestDriveBooking> testDriveBookings() {
        return testDriveBookings;
    }

    public Optional<Car> findByVin(String vin) {
        if (vin == null) {
            return Optional.empty();
        }
        return cars.stream()
                .filter(car -> car.vin().equalsIgnoreCase(vin))
                .findFirst();
    }

    public DiscountResult prepareDiscountPreview(String vin, BigDecimal discountFactor) {
        if (vin == null || discountFactor == null) {
            return new DiscountResult(null, discountHistory());
        }

        return findByVin(vin)
                .map(car -> {
                    var normalized = normalizeDiscount(discountFactor);
                    var discountedPrice = car.price()
                            .multiply(BigDecimal.ONE.subtract(normalized))
                            .max(BigDecimal.ZERO)
                            .setScale(2, RoundingMode.HALF_UP);
                    var preview = car.withPrice(discountedPrice)
                            .withAddedNote("Rabattvorschau: " + normalized.multiply(BigDecimal.valueOf(100)).intValue() + "%");

                    var updatedHistory = new LinkedHashMap<>(discountHistory.get());
                    updatedHistory.put(vin, normalized);
                    discountHistory.set(Map.copyOf(updatedHistory));

                    return new DiscountResult(preview, discountHistory());
                })
                .orElse(new DiscountResult(null, discountHistory()));
    }

    public Map<String, BigDecimal> discountHistory() {
        return discountHistory.get();
    }

    private static DiscountResultSeed seedData() {
        var kai = new Customer("Kai Berger", "kai.berger@example.com", null);
        var mira = new Customer("Mira Lopez", "mira.lopez@example.com", null);
        var elise = new Customer("Elise König", "elise.koenig@example.com", null);

        var laura = new SalesConsultant("Laura Sommer", "laura.sommer@autohof.example", List.of());
        var deniz = new SalesConsultant("Deniz Hartung", "deniz.hartung@autohof.example", List.of());

        var id4 = new Car("WVWZZZ1KZ6W000001", "Volkswagen", "ID.4",
                LocalDate.of(2022, 3, 12), new BigDecimal("38990"), EnergyType.BEV,
                laura, List.of("Vorführwagen - leichte Gebrauchsspuren", "Software-Update Q1/2024 installiert"));

        var q4 = new Car("WAUZZZF25MN000123", "Audi", "Q4 e-tron",
                LocalDate.of(2023, 6, 5), new BigDecimal("52990"), EnergyType.BEV,
                laura, List.of("Business-Ausführung mit Assistenzpaket"));

        var c200 = new Car("WDD2221231A000987", "Mercedes-Benz", "C 200",
                LocalDate.of(2021, 9, 21), new BigDecimal("44990"), EnergyType.MHEV,
                deniz, List.of("Scheckheft gepflegt"));

        var xc40 = new Car("YV1XZ16A6M1012345", "Volvo", "XC40 Recharge",
                LocalDate.of(2022, 11, 18), new BigDecimal("47990"), EnergyType.BEV,
                deniz, List.of("Wartung neu, inklusive Winterräder"));

        var leaf = new Car("SJNFAAZE1U0004321", "Nissan", "Leaf Tekna",
                LocalDate.of(2020, 2, 9), new BigDecimal("21490"), EnergyType.BEV,
                deniz, List.of("Batteriezustand 92%"));

        kai = kai.withPreferredVin(id4.vin());
        mira = mira.withPreferredVin(q4.vin());
        elise = elise.withPreferredVin(c200.vin());

        laura = laura.withAssignedCustomers(List.of(kai, mira));
        deniz = deniz.withAssignedCustomers(List.of(elise));

        id4 = id4.withConsultant(laura);
        q4 = q4.withConsultant(laura);
        c200 = c200.withConsultant(deniz);
        xc40 = xc40.withConsultant(deniz);
        leaf = leaf.withConsultant(deniz);

        var offerKai = new LeasingOffer("LEASE-2001", new BigDecimal("5000"), 36, BigDecimal.ZERO, kai, id4)
                .recalculateForPrice(id4.price())
                .adjustRate(new BigDecimal("15.00"));

        var offerMira = new LeasingOffer("LEASE-2002", new BigDecimal("6500"), 48, BigDecimal.ZERO, mira, q4)
                .recalculateForPrice(q4.price());

        var offerElise = new LeasingOffer("LEASE-2003", new BigDecimal("3000"), 24, BigDecimal.ZERO, elise, c200)
                .recalculateForPrice(c200.price());

        var bookingKai = new TestDriveBooking(kai, id4,
                LocalDateTime.now().plusDays(1).withHour(10).withMinute(30), BookingStatus.CONFIRMED);

        var bookingMira = new TestDriveBooking(mira, q4,
                LocalDateTime.now().plusDays(2).withHour(14).withMinute(0), BookingStatus.PENDING)
                .reschedule(LocalDateTime.now().plusDays(2).withHour(16).withMinute(0))
                .updateStatus(BookingStatus.CONFIRMED);

        var bookingElise = new TestDriveBooking(elise, c200,
                LocalDateTime.now().plusDays(3).withHour(9).withMinute(45), BookingStatus.WAITING_LIST);

        return new DiscountResultSeed(
                List.of(id4, q4, c200, xc40, leaf),
                List.of(offerKai, offerMira, offerElise),
                List.of(bookingKai, bookingMira, bookingElise)
        );
    }

    private BigDecimal normalizeDiscount(BigDecimal discount) {
        if (discount.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }
        if (discount.compareTo(new BigDecimal("0.60")) > 0) {
            return new BigDecimal("0.60");
        }
        return discount.setScale(2, RoundingMode.HALF_UP);
    }

    public record DiscountResult(Car preview, Map<String, BigDecimal> history) {
    }

    private record DiscountResultSeed(
            List<Car> cars,
            List<LeasingOffer> leasingOffers,
            List<TestDriveBooking> testDriveBookings
    ) {
    }
}
