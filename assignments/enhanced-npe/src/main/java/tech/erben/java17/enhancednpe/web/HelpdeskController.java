package tech.erben.java17.enhancednpe.web;

import tech.erben.java17.enhancednpe.service.SvgSceneService;
import tech.erben.java17.enhancednpe.service.TicketDataService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelpdeskController {

    private final TicketDataService ticketDataService;
    private final SvgSceneService svgSceneService;

    public HelpdeskController(TicketDataService ticketDataService, SvgSceneService svgSceneService) {
        this.ticketDataService = ticketDataService;
        this.svgSceneService = svgSceneService;
    }

    @GetMapping("/")
    public String show(Model model) {
        var tickets = ticketDataService.sampleTickets();
        var svgMarkup = svgSceneService.renderBoard(tickets);

        model.addAttribute("svgMarkup", svgMarkup);
        model.addAttribute("tickets", tickets);
        return "dashboard";
    }
}
