package tech.erben.java17.randomraffle.solution.model;

import java.util.List;

public record Winner(Participant participant, Prize prize, List<Integer> ticketNumbers, long ticketId) {
}
