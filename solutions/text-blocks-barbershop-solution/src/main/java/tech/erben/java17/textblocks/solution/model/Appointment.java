package tech.erben.java17.textblocks.solution.model;

import java.time.LocalDateTime;

public record Appointment(
        String customerName,
        String barberName,
        LocalDateTime slot,
        String treatment
) {
}
