package tech.erben.java17.textblocks.model;

import java.time.LocalDateTime;

/**
 * Repräsentiert einen Terminplatz im Barbershop.
 */
public record Appointment(
        String customerName,
        String barberName,
        LocalDateTime slot,
        String treatment
) {
}
