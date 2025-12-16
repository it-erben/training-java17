package tech.erben.java17.fooddelivery.solution.web;

import tech.erben.java17.fooddelivery.solution.service.DeliveryBoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DeliveryController {

    private final DeliveryBoardService boardService;

    public DeliveryController(DeliveryBoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/")
    public String board(Model model) {
        var deliveries = boardService.deliveries();
        model.addAttribute("deliveries", deliveries);
        model.addAttribute("sortedDeliveries", boardService.sortedByEta(deliveries));
        model.addAttribute("summary", boardService.statusSummary(deliveries));
        model.addAttribute("board", boardService);
        return "board";
    }
}
