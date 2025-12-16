package tech.erben.gfu.records;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class RecordPlayground {
    public static void main(String[] args) {
        Participant participant = new Participant("Mia", "Meyer", "mia@example.com", "ACME GmbH");
        MailingAddress address = MailingAddress.of("Hauptstr. 12", "50667", "Koeln", "de");
        WorkshopRegistration registration = new WorkshopRegistration(
                participant,
                address,
                List.of("Records Basics", "Refactoring mit Records"),
                new BigDecimal("249.00"),
                LocalDate.now());

        System.out.println(registration.summary());

        WorkshopRegistration withExtraModule = registration.withAddedModule("Bonus: Pattern Matching");
        Participant updatedParticipant = participant.withEmail("mia@acme.test");
        WorkshopRegistration withUpdatedParticipant = withExtraModule.withParticipant(updatedParticipant);

        MailingAddress addressCopy = address.withPostalCode("10115");
        WorkshopRegistration withAddressCopy = withUpdatedParticipant.withBillingAddress(addressCopy);

        System.out.println("\nKopie mit geänderten Modulen und alternativer Adresse:");
        System.out.println("Original-PLZ: " + registration.billingAddress().postalCode());
        System.out.println("Kopie-PLZ: " + withAddressCopy.billingAddress().postalCode());
        System.out.println("Original-Module: " + registration.bookedModules());
        System.out.println("Kopie-Module: " + withAddressCopy.bookedModules());
        System.out.println("Neue Teilnehmer-Mail (immutable): " + withUpdatedParticipant.participant().email());
        System.out.println("Gesamtpreis der Kopie: " + withAddressCopy.calculateTotalPrice());
    }
}
