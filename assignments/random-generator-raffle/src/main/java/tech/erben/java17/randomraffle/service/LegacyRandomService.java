package tech.erben.java17.randomraffle.service;

import org.springframework.stereotype.Service;
import tech.erben.java17.randomraffle.model.Participant;
import tech.erben.java17.randomraffle.model.Prize;
import tech.erben.java17.randomraffle.model.Winner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class LegacyRandomService {

    private final RaffleDataRepository repository;

    private final Random random = new Random();
    private Random seeded = new Random(System.nanoTime());

    public LegacyRandomService(RaffleDataRepository repository) {
        this.repository = repository;
    }

    public Winner drawWinner() {
        var participants = repository.participants();
        var prizes = new ArrayList<>(repository.prizes());

        if (participants.isEmpty() || prizes.isEmpty()) {
            throw new IllegalStateException("Teilnehmer oder Preise fehlen.");
        }

        var index = Math.abs(random.nextInt()) % participants.size();
        var participant = participants.get(index);

        Collections.shuffle(prizes, random);
        var prize = prizes.get(0);

        var ticketNumbers = generateTicketNumbers(6);
        var ticketId = Math.abs(random.nextLong());

        return new Winner(participant, prize, ticketNumbers, ticketId);
    }

    public List<Integer> generateTicketNumbers(int amount) {
        var numbers = new ArrayList<Integer>();
        for (int i = 0; i < amount; i++) {
            numbers.add(Math.abs(seeded.nextInt()) % 50 + 1);
        }
        return numbers;
    }

    public Map<String, Double> simulatePrizeDistribution(int draws) {
        var prizes = repository.prizes();
        if (draws <= 0) {
            draws = 1000;
        }
        var hits = new HashMap<Prize, Integer>();
        for (Prize prize : prizes) {
            hits.put(prize, 0);
        }

        for (int i = 0; i < draws; i++) {
            var selector = new Random();
            var prize = prizes.get(selector.nextInt(prizes.size()));
            hits.computeIfPresent(prize, (key, value) -> value + 1);
        }

        var distribution = new HashMap<String, Double>();
        for (var entry : hits.entrySet()) {
            distribution.put(entry.getKey().name(), entry.getValue() / (double) draws);
        }
        return distribution;
    }

    public List<Long> previewTicketIds(int amount) {
        var ids = new ArrayList<Long>();
        for (int i = 0; i < amount; i++) {
            ids.add(Math.abs(random.nextLong()));
        }
        seeded = new Random(System.nanoTime());
        return ids;
    }
}
