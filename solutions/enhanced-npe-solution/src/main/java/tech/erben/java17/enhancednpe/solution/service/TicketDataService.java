package tech.erben.java17.enhancednpe.solution.service;

import tech.erben.java17.enhancednpe.solution.model.Agent;
import tech.erben.java17.enhancednpe.solution.model.SupportTicket;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketDataService {

    public List<SupportTicket> sampleTickets() {
        var clara = new Agent("Clara Krüger", "clara@gfu.de", "#6a1b9a");
        var jonas = new Agent("Jonas Berger", "jonas@gfu.de", null);
        var sven = new Agent("Sven Adler", null, "#ff7043");
        var priya = new Agent("Priya Desai", "priya@gfu.de", "#3949ab");

        return List.of(
                new SupportTicket("HD-10", "Onboarding", priya, 0.05),
                new SupportTicket("HD-42", "VPN bricht ab", clara, 0.35),
                new SupportTicket("HD-55", "Laptop defekt", priya, 0.62),
                new SupportTicket("HD-60", null, jonas, 0.82),
                new SupportTicket("HD-73", "CI Pipeline", sven, null),
                new SupportTicket("HD-88", "Redesign", null, 0.12),
                new SupportTicket(null, "Lizenzverlängerung", clara, 0.95),
                new SupportTicket("HD-99", "Server-Down!", null, null)
        );
    }
}
