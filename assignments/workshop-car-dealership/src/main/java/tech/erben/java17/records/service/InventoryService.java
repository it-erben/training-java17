package tech.erben.java17.records.service;

import tech.erben.java17.records.model.Car;
import tech.erben.java17.records.model.Customer;
import tech.erben.java17.records.model.LeasingOffer;
import tech.erben.java17.records.model.SalesConsultant;
import tech.erben.java17.records.model.TestDriveBooking;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class InventoryService {

    private final List<Car> cars = new ArrayList<>();
    private final List<LeasingOffer> leasingOffers = new ArrayList<>();
    private final List<TestDriveBooking> testDriveBookings = new ArrayList<>();
    private final Map<String, BigDecimal> discountCache = new HashMap<>();

    public InventoryService() {
        seedData();
    }

    public List<Car> inventory() {
        return cars;
    }

    public Optional<Car> findByVin(String vin) {
        return cars.stream()
                .filter(car -> car.getVin().equalsIgnoreCase(vin))
                .findFirst();
    }

    public Car prepareDiscountPreview(String vin, BigDecimal discountFactor) {
        if (vin == null || discountFactor == null) {
            return null;
        }
        var car = findByVin(vin).orElse(null);
        if (car == null) {
            return null;
        }
        var provenDiscount = normalizeDiscount(discountFactor);

        var originalPrice = car.getPrice();
        var discountedPrice = originalPrice.subtract(originalPrice.multiply(provenDiscount));
        if (discountedPrice.compareTo(BigDecimal.ZERO) < 0) {
            discountedPrice = BigDecimal.ZERO;
        }

        // Mutiert den Fahrzeugzustand, damit abhängige Strukturen neu berechnet werden.
        car.setPrice(discountedPrice);
        updateDependentStructures(car);

        // Baut eine Vorschau zusammen, indem Feld für Feld kopiert wird.
        var preview = new Car();
        preview.setVin(car.getVin());
        preview.setBrand(car.getBrand());
        preview.setModel(car.getModel());
        preview.setFirstRegistration(car.getFirstRegistration());
        preview.setPrice(discountedPrice);
        preview.setEnergyType(car.getEnergyType());
        preview.setConsultant(car.getConsultant());
        car.getNotes().forEach(preview::addNote);

        // Ausgangszustand wiederherstellen, um Seiteneffekte zu vermeiden.
        car.setPrice(originalPrice);
        updateDependentStructures(car);

        discountCache.put(vin, provenDiscount);
        return preview;
    }

    public Map<String, BigDecimal> getDiscountCache() {
        return discountCache;
    }

    public List<LeasingOffer> getLeasingOffers() {
        return leasingOffers;
    }

    public List<TestDriveBooking> getTestDriveBookings() {
        return testDriveBookings;
    }

    private void seedData() {
        var customerKai = new Customer();
        customerKai.setName("Kai Berger");
        customerKai.setEmail("kai.berger@example.com");

        var customerMira = new Customer();
        customerMira.setName("Mira Lopez");
        customerMira.setEmail("mira.lopez@example.com");

        var customerElise = new Customer();
        customerElise.setName("Elise König");
        customerElise.setEmail("elise.koenig@example.com");

        var consultantLaura = new SalesConsultant();
        consultantLaura.setName("Laura Sommer");
        consultantLaura.setEmail("laura.sommer@autohof.example");
        consultantLaura.addCustomer(customerKai);
        consultantLaura.addCustomer(customerMira);

        var consultantDeniz = new SalesConsultant();
        consultantDeniz.setName("Deniz Hartung");
        consultantDeniz.setEmail("deniz.hartung@autohof.example");
        consultantDeniz.addCustomer(customerElise);

        var id4 = new Car();
        id4.setVin("WVWZZZ1KZ6W000001");
        id4.setBrand("Volkswagen");
        id4.setModel("ID.4");
        id4.setFirstRegistration(LocalDate.of(2022, 3, 12));
        id4.setPrice(new BigDecimal("38990"));
        id4.setEnergyType("BEV");
        id4.setConsultant(consultantLaura);
        id4.addNote("Vorführwagen - leichte Gebrauchsspuren");
        id4.addNote("Software-Update Q1/2024 installiert");
        customerKai.setPreferredVin(id4.getVin());

        var q4 = new Car();
        q4.setVin("WAUZZZF25MN000123");
        q4.setBrand("Audi");
        q4.setModel("Q4 e-tron");
        q4.setFirstRegistration(LocalDate.of(2023, 6, 5));
        q4.setPrice(new BigDecimal("52990"));
        q4.setEnergyType("BEV");
        q4.setConsultant(consultantLaura);
        q4.addNote("Business-Ausführung mit Assistenzpaket");
        customerMira.setPreferredVin(q4.getVin());

        var c200 = new Car();
        c200.setVin("WDD2221231A000987");
        c200.setBrand("Mercedes-Benz");
        c200.setModel("C 200");
        c200.setFirstRegistration(LocalDate.of(2021, 9, 21));
        c200.setPrice(new BigDecimal("44990"));
        c200.setEnergyType("MHEV");
        c200.setConsultant(consultantDeniz);
        c200.addNote("Scheckheft gepflegt");
        customerElise.setPreferredVin(c200.getVin());

        var xc40 = new Car();
        xc40.setVin("YV1XZ16A6M1012345");
        xc40.setBrand("Volvo");
        xc40.setModel("XC40 Recharge");
        xc40.setFirstRegistration(LocalDate.of(2022, 11, 18));
        xc40.setPrice(new BigDecimal("47990"));
        xc40.setEnergyType("BEV");
        xc40.setConsultant(consultantDeniz);
        xc40.addNote("Wartung neu, inklusive Winterräder");

        var leaf = new Car();
        leaf.setVin("SJNFAAZE1U0004321");
        leaf.setBrand("Nissan");
        leaf.setModel("Leaf Tekna");
        leaf.setFirstRegistration(LocalDate.of(2020, 2, 9));
        leaf.setPrice(new BigDecimal("21490"));
        leaf.setEnergyType("BEV");
        leaf.setConsultant(consultantDeniz);
        leaf.addNote("Batteriezustand 92%");

        cars.add(id4);
        cars.add(q4);
        cars.add(c200);
        cars.add(xc40);
        cars.add(leaf);

        var offerKai = new LeasingOffer();
        offerKai.setOfferId("LEASE-1001");
        offerKai.setCustomer(customerKai);
        offerKai.setCar(id4);
        offerKai.setDownPayment(new BigDecimal("5000"));
        offerKai.setDurationMonths(36);
        offerKai.recalculateMonthlyRate(id4.getPrice());
        // Manuelle Nachjustierung durch den Vertrieb.
        offerKai.setMonthlyRate(offerKai.getMonthlyRate().add(new BigDecimal("15.00")));

        var offerMira = new LeasingOffer();
        offerMira.setOfferId("LEASE-1002");
        offerMira.setCustomer(customerMira);
        offerMira.setCar(q4);
        offerMira.setDownPayment(new BigDecimal("6500"));
        offerMira.setDurationMonths(48);
        offerMira.recalculateMonthlyRate(q4.getPrice());

        var offerElise = new LeasingOffer();
        offerElise.setOfferId("LEASE-1003");
        offerElise.setCustomer(customerElise);
        offerElise.setCar(c200);
        offerElise.setDownPayment(new BigDecimal("3000"));
        offerElise.setDurationMonths(24);
        offerElise.recalculateMonthlyRate(c200.getPrice());

        leasingOffers.add(offerKai);
        leasingOffers.add(offerMira);
        leasingOffers.add(offerElise);

        var bookingKai = new TestDriveBooking();
        bookingKai.setCustomer(customerKai);
        bookingKai.setCar(id4);
        bookingKai.setSlot(LocalDateTime.now().plusDays(1).withHour(10).withMinute(30));
        bookingKai.setStatus("CONFIRMED");

        var bookingMira = new TestDriveBooking();
        bookingMira.setCustomer(customerMira);
        bookingMira.setCar(q4);
        bookingMira.setSlot(LocalDateTime.now().plusDays(2).withHour(14).withMinute(0));
        bookingMira.setStatus("PENDING");
        bookingMira.reschedule(bookingMira.getSlot().plusHours(2));
        bookingMira.setStatus("CONFIRMED");

        var bookingElise = new TestDriveBooking();
        bookingElise.setCustomer(customerElise);
        bookingElise.setCar(c200);
        bookingElise.setSlot(LocalDateTime.now().plusDays(3).withHour(9).withMinute(45));
        bookingElise.setStatus("WAITING_LIST");

        testDriveBookings.add(bookingKai);
        testDriveBookings.add(bookingMira);
        testDriveBookings.add(bookingElise);
    }

    private BigDecimal normalizeDiscount(BigDecimal discountFactor) {
        if (discountFactor.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }
        if (discountFactor.compareTo(new BigDecimal("0.60")) > 0) {
            return new BigDecimal("0.60");
        }
        return discountFactor;
    }

    private void updateDependentStructures(Car referenceCar) {
        for (LeasingOffer offer : leasingOffers) {
            if (offer.getCar() == referenceCar) {
                offer.recalculateMonthlyRate(referenceCar.getPrice());
                offer.setMonthlyRate(offer.getMonthlyRate().add(new BigDecimal("5.00")));
            }
        }
        for (TestDriveBooking booking : testDriveBookings) {
            if (booking.getCar() == referenceCar && booking.getStatus().equals("WAITING_LIST")) {
                booking.setStatus("REVIEW_PRICE_DROP");
            }
        }
    }
}
