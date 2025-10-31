package tech.erben.java17.randomraffle.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.erben.java17.randomraffle.model.Winner;
import tech.erben.java17.randomraffle.service.LegacyRandomService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/raffle")
public class RaffleController {

    private final LegacyRandomService randomService;

    public RaffleController(LegacyRandomService randomService) {
        this.randomService = randomService;
    }

    @GetMapping("/winner")
    public Winner winner() {
        return randomService.drawWinner();
    }

    @GetMapping("/tickets")
    public List<Integer> tickets(@RequestParam(defaultValue = "6") int amount) {
        return randomService.generateTicketNumbers(amount);
    }

    @GetMapping("/distribution")
    public Map<String, Double> distribution(@RequestParam(defaultValue = "5000") int draws) {
        return randomService.simulatePrizeDistribution(draws);
    }

    @GetMapping("/ticket-ids")
    public List<Long> ticketIds(@RequestParam(defaultValue = "5") int amount) {
        return randomService.previewTicketIds(amount);
    }
}
