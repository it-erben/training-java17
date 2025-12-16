package tech.erben.gfu.records;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record WorkshopRegistration(Participant participant,
                                   MailingAddress billingAddress,
                                   List<String> bookedModules,
                                   BigDecimal pricePerModule,
                                   LocalDate registeredAt) {

    public WorkshopRegistration {
        participant = Objects.requireNonNull(participant, "participant must not be null");
        billingAddress = Objects.requireNonNull(billingAddress, "billingAddress must not be null");
        pricePerModule = Objects.requireNonNull(pricePerModule, "pricePerModule must not be null");
        bookedModules = bookedModules == null ? List.of() : List.copyOf(bookedModules);
        registeredAt = registeredAt == null ? LocalDate.now() : registeredAt;
    }

    public WorkshopRegistration withAddedModule(String module) {
        String cleaned = Objects.requireNonNull(module, "module must not be null").trim();
        List<String> modules = new ArrayList<>(bookedModules);
        modules.add(cleaned);
        return new WorkshopRegistration(participant, billingAddress, modules, pricePerModule, registeredAt);
    }

    public WorkshopRegistration withPricePerModule(BigDecimal newPricePerModule) {
        return new WorkshopRegistration(participant, billingAddress, bookedModules, newPricePerModule, registeredAt);
    }

    public WorkshopRegistration withParticipant(Participant newParticipant) {
        return new WorkshopRegistration(newParticipant, billingAddress, bookedModules, pricePerModule, registeredAt);
    }

    public WorkshopRegistration withBillingAddress(MailingAddress newBillingAddress) {
        return new WorkshopRegistration(participant, newBillingAddress, bookedModules, pricePerModule, registeredAt);
    }

    public BigDecimal calculateTotalPrice() {
        return pricePerModule.multiply(BigDecimal.valueOf(bookedModules.size()));
    }

    public String summary() {
        return participant.fullName() + " (" + participant.email() + ") -> "
                + String.join(", ", bookedModules)
                + " | Preis pro Modul: " + pricePerModule
                + " | registriert am " + registeredAt;
    }
}
