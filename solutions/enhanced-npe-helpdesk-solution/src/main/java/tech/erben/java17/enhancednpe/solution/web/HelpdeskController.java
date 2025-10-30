package tech.erben.java17.enhancednpe.solution.web;

import tech.erben.java17.enhancednpe.solution.service.SvgBoardRenderer;
import tech.erben.java17.enhancednpe.solution.service.TicketDataService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelpdeskController {

    private final TicketDataService ticketDataService;
    private final SvgBoardRenderer svgBoardRenderer;

    public HelpdeskController(TicketDataService ticketDataService, SvgBoardRenderer svgBoardRenderer) {
        this.ticketDataService = ticketDataService;
        this.svgBoardRenderer = svgBoardRenderer;
    }

    @GetMapping("/")
    public String overview(Model model) {
        var tickets = ticketDataService.sampleTickets();
        var svgMarkup = svgBoardRenderer.renderBoard(tickets);

        model.addAttribute("svgMarkup", svgMarkup);
        model.addAttribute("tickets", tickets);
        return "helpdesk";
    }
}
