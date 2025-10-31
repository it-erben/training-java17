package tech.erben.java17.randomraffle.solution.service;

import org.springframework.stereotype.Service;
import tech.erben.java17.randomraffle.solution.model.Participant;
import tech.erben.java17.randomraffle.solution.model.Prize;
import tech.erben.java17.randomraffle.solution.model.Winner;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;
import java.util.stream.Collectors;

@Service
public class RaffleRandomService {

    private final RaffleDataRepository repository;
    private final RandomGeneratorFactory<? extends RandomGenerator> generatorFactory;
    private final long baseSeed;

    public RaffleRandomService(RaffleDataRepository repository) {
        this.repository = repository;
        this.generatorFactory = RandomGeneratorFactory.of("L64X128MixRandom");
        this.baseSeed = Long.getLong("raffle.seed", 20240517L);
    }

    public Winner drawWinner(Long seedOverride) {
        var rng = seedOverride != null ? generatorFactory.create(seedOverride) : generatorFor("winner");
        var participants = repository.participants();
        var prizes = repository.prizes();
        ensureDataAvailable(participants, prizes);

        var participant = pick(participants, rng);
        var prize = pick(prizes, rng);
        var ticketNumbers = rng.ints(6, 1, 51).boxed().collect(Collectors.toList());
        var ticketId = Math.abs(rng.nextLong());

        return new Winner(participant, prize, ticketNumbers, ticketId);
    }

    public List<Integer> ticketNumbers(int amount, Long seedOverride) {
        var size = Math.max(1, amount);
        var rng = seedOverride != null ? generatorFactory.create(seedOverride) : generatorFor("tickets:" + size);
        return rng.ints(size, 1, 51).boxed().collect(Collectors.toList());
    }

    public List<Long> ticketIds(int amount) {
        var size = Math.max(1, amount);
        var rng = generatorFor("ticket-ids:" + size);
        return rng.longs(size, 0L, Long.MAX_VALUE).boxed().collect(Collectors.toList());
    }

    public Map<String, Map<String, Double>> simulateDistributions(int draws) {
        var iterations = draws > 0 ? draws : 5000;
        var prizes = repository.prizes();
        var regions = repository.participants().stream()
                .map(Participant::region)
                .distinct()
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());

        var root = rootSplittable(0x9E3779B97F4A7C15L);
        var generators = root.splits(regions.size())
                .limit(regions.size())
                .collect(Collectors.toList());

        var result = new LinkedHashMap<String, Map<String, Double>>();
        for (int i = 0; i < regions.size(); i++) {
            var region = regions.get(i);
            var regionGenerator = generators.get(i);
            var hits = new long[prizes.size()];
            regionGenerator.ints(iterations, 0, prizes.size())
                    .forEach(index -> hits[index]++);

            var distribution = new LinkedHashMap<String, Double>();
            for (int j = 0; j < prizes.size(); j++) {
                distribution.put(prizes.get(j).name(), hits[j] / (double) iterations);
            }
            result.put(region, distribution);
        }
        return result;
    }

    public List<String> availableGenerators() {
        return RandomGeneratorFactory.all()
                .filter(factory -> factory.group().equalsIgnoreCase("LXM"))
                .filter(RandomGeneratorFactory::isSplittable)
                .map(RandomGeneratorFactory::name)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    private RandomGenerator generatorFor(String context) {
        long contextSeed = baseSeed ^ context.hashCode();
        return generatorFactory.create(contextSeed);
    }

    private RandomGenerator.SplittableGenerator rootSplittable(long salt) {
        var generator = generatorFactory.create(baseSeed + salt);
        if (generator instanceof RandomGenerator.SplittableGenerator splittable) {
            return splittable;
        }
        return RandomGenerator.SplittableGenerator.of(generatorFactory.name());
    }

    private static <T> T pick(List<T> items, RandomGenerator generator) {
        int index = generator.nextInt(items.size());
        return items.get(index);
    }

    private static void ensureDataAvailable(List<Participant> participants, List<Prize> prizes) {
        Objects.requireNonNull(participants, "Teilnehmerliste fehlt");
        Objects.requireNonNull(prizes, "Preisliste fehlt");
        if (participants.isEmpty() || prizes.isEmpty()) {
            throw new IllegalStateException("Teilnehmer oder Preise fehlen.");
        }
    }
}
