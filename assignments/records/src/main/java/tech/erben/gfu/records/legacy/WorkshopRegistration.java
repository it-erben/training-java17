package tech.erben.gfu.records.legacy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WorkshopRegistration {
    private Participant participant;
    private MailingAddress billingAddress;
    private List<String> bookedModules;
    private BigDecimal pricePerModule;
    private LocalDate registeredAt;

    public WorkshopRegistration(Participant participant,
                                MailingAddress billingAddress,
                                List<String> bookedModules,
                                BigDecimal pricePerModule,
                                LocalDate registeredAt) {
        this.participant = participant;
        this.billingAddress = billingAddress;
        this.bookedModules = bookedModules == null ? new ArrayList<>() : new ArrayList<>(bookedModules);
        this.pricePerModule = pricePerModule;
        this.registeredAt = registeredAt;
    }

    public Participant getParticipant() {
        return participant;
    }

    public MailingAddress getBillingAddress() {
        return billingAddress;
    }

    public List<String> getBookedModules() {
        return bookedModules;
    }

    public BigDecimal getPricePerModule() {
        return pricePerModule;
    }

    public LocalDate getRegisteredAt() {
        return registeredAt;
    }

    public void addModule(String module) {
        bookedModules.add(module);
    }

    public BigDecimal calculateTotalPrice() {
        return pricePerModule.multiply(BigDecimal.valueOf(bookedModules.size()));
    }

    public String summary() {
        return participant.fullName() + " (" + participant.getEmail() + ") -> "
                + String.join(", ", bookedModules)
                + " | Preis pro Modul: " + pricePerModule
                + " | registriert am " + registeredAt;
    }

    public WorkshopRegistration copy() {
        return new WorkshopRegistration(participant, billingAddress, bookedModules, pricePerModule, registeredAt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkshopRegistration that)) return false;
        return Objects.equals(participant, that.participant)
                && Objects.equals(billingAddress, that.billingAddress)
                && Objects.equals(bookedModules, that.bookedModules)
                && Objects.equals(pricePerModule, that.pricePerModule)
                && Objects.equals(registeredAt, that.registeredAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(participant, billingAddress, bookedModules, pricePerModule, registeredAt);
    }

    @Override
    public String toString() {
        return "WorkshopRegistration{" +
                "participant=" + participant +
                ", billingAddress=" + billingAddress +
                ", bookedModules=" + bookedModules +
                ", pricePerModule=" + pricePerModule +
                ", registeredAt=" + registeredAt +
                '}';
    }
}
