package tech.erben.java17.randomraffle.solution.service;

import org.springframework.stereotype.Component;
import tech.erben.java17.randomraffle.solution.model.Participant;
import tech.erben.java17.randomraffle.solution.model.Prize;

import java.util.List;

@Component
public class RaffleDataRepository {

    private final List<Participant> participants = List.of(
            new Participant("Mira Hoffmann", "mira.hoffmann@example.com", "DE"),
            new Participant("Jonas Fischer", "jonas.fischer@example.com", "DE"),
            new Participant("Lucia Ortega", "lucia.ortega@example.com", "ES"),
            new Participant("Noah Zielinski", "noah.zielinski@example.com", "PL"),
            new Participant("Amelia Chen", "amelia.chen@example.com", "SG"),
            new Participant("Leo Martins", "leo.martins@example.com", "BR")
    );

    private final List<Prize> prizes = List.of(
            new Prize("Konferenzreise", 1, 1),
            new Prize("Noise-Cancelling-Kopfhörer", 3, 2),
            new Prize("Gourmet-Gutschein", 10, 3),
            new Prize("Coaching-Session", 5, 2),
            new Prize("Team-Lunch", 8, 3),
            new Prize("Community-Shoutout", 20, 4)
    );

    public List<Participant> participants() {
        return participants;
    }

    public List<Prize> prizes() {
        return prizes;
    }
}
