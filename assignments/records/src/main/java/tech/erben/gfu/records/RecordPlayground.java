package tech.erben.gfu.records;

import tech.erben.gfu.records.legacy.MailingAddress;
import tech.erben.gfu.records.legacy.Participant;
import tech.erben.gfu.records.legacy.WorkshopRegistration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class RecordPlayground {
    public static void main(String[] args) {
        Participant participant = new Participant("Mia", "Meyer", "mia@example.com", "ACME GmbH");
        MailingAddress address = new MailingAddress("Hauptstr. 12", "50667", "Koeln", "de");
        WorkshopRegistration registration = new WorkshopRegistration(participant, address, List.of("Records Basics", "Refactoring mit Records"), new BigDecimal("249.00"), LocalDate.now());

        System.out.println(registration.summary());
        registration.addModule("Bonus: Pattern Matching");
        participant.changeEmail("mia@acme.test");

        System.out.println("Aktuelle Module: " + registration.getBookedModules());
        System.out.println("Aktueller Gesamtpreis: " + registration.calculateTotalPrice());
        System.out.println("Mutable Teilnehmer-E-Mail (sollte später nicht mehr gehen): " + registration.getParticipant().getEmail());

        MailingAddress addressCopy = address.copyWithPostalCode("10115");
        WorkshopRegistration registrationCopy = registration.copy();
        registrationCopy.addModule("Records Deep Dive");

        System.out.println("\nKopie mit geänderten Modulen und alternativer Adresse:");
        System.out.println("Original-PLZ: " + registration.getBillingAddress().getPostalCode());
        System.out.println("Kopie-PLZ: " + addressCopy.getPostalCode());
        System.out.println("Original-Module: " + registration.getBookedModules());
        System.out.println("Kopie-Module: " + registrationCopy.getBookedModules());
    }
}
