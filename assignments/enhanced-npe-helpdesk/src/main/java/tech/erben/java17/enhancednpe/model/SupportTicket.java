package tech.erben.java17.enhancednpe.model;

public record SupportTicket(
        String id,
        String summary,
        Agent assignee,
        Double progress
) {
}
