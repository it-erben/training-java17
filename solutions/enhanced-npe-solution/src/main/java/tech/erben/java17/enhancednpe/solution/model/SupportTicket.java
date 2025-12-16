package tech.erben.java17.enhancednpe.solution.model;

public record SupportTicket(
        String id,
        String summary,
        Agent assignee,
        Double progress
) {
}
