package tech.erben.java17.textblocks.solution.web;

import tech.erben.java17.textblocks.solution.model.Appointment;
import tech.erben.java17.textblocks.solution.service.AppointmentTemplateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class AppointmentController {

    private final AppointmentTemplateService templateService;

    public AppointmentController(AppointmentTemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping("/")
    public String templates(Model model) {
        var sampleAppointment = new Appointment("Alex", "Fatima", LocalDateTime.now().plusDays(1), "Deluxe Fade");
        var nextAppointments = List.of(
                sampleAppointment,
                new Appointment("Mara", "Luis", LocalDateTime.now().plusDays(2).withHour(14).withMinute(30), "Bartpflege"),
                new Appointment("Noah", "Fatima", LocalDateTime.now().plusDays(3).withHour(10).withMinute(0), "Klassischer Schnitt"),
                new Appointment("Timo", "Hannah", LocalDateTime.now().plusDays(4).withHour(16).withMinute(0), "Skin Fade"),
                new Appointment("Sara", "Luis", LocalDateTime.now().plusDays(5).withHour(11).withMinute(30), "Pflegepaket")
        );

        model.addAttribute("confirmation", templateService.buildConfirmation(sampleAppointment));
        model.addAttribute("overview", templateService.buildWeeklyOverview(nextAppointments));
        model.addAttribute("htmlEmail", templateService.buildHtmlEmail(sampleAppointment));
        model.addAttribute("markdown", templateService.buildMarkdownPreview(sampleAppointment));
        model.addAttribute("promotion", templateService.buildIndentedPromotion());
        model.addAttribute("escapes", templateService.buildEscapedSnippet());
        model.addAttribute("sql", templateService.buildSqlSnippet("Luis"));
        return "templates";
    }
}
