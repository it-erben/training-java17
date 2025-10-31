package tech.erben.java17.randomraffle.solution.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.erben.java17.randomraffle.solution.model.Winner;
import tech.erben.java17.randomraffle.solution.service.RaffleRandomService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/raffle")
public class RaffleController {

    private final RaffleRandomService randomService;

    public RaffleController(RaffleRandomService randomService) {
        this.randomService = randomService;
    }

    @GetMapping("/winner")
    public Winner winner(@RequestParam(value = "seed", required = false) Long seed) {
        return randomService.drawWinner(seed);
    }

    @GetMapping("/tickets")
    public List<Integer> tickets(@RequestParam(defaultValue = "6") int amount,
                                 @RequestParam(value = "seed", required = false) Long seed) {
        return randomService.ticketNumbers(amount, seed);
    }

    @GetMapping("/ticket-ids")
    public List<Long> ticketIds(@RequestParam(defaultValue = "5") int amount) {
        return randomService.ticketIds(amount);
    }

    @GetMapping("/distribution")
    public Map<String, Map<String, Double>> distribution(@RequestParam(defaultValue = "5000") int draws) {
        return randomService.simulateDistributions(draws);
    }

    @GetMapping("/generators")
    public List<String> generators() {
        return randomService.availableGenerators();
    }
}
