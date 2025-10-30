package tech.erben.java17.textblocks.web;

import tech.erben.java17.textblocks.model.Appointment;
import tech.erben.java17.textblocks.service.AppointmentTemplateService;
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
    public String showTemplates(Model model) {
        var sampleAppointment = new Appointment("Alex", "Fatima", LocalDateTime.now().plusDays(1), "Deluxe Fade");
        var nextAppointments = List.of(
                sampleAppointment,
                new Appointment("Mara", "Luis", LocalDateTime.now().plusDays(2).withHour(14).withMinute(30), "Bartpflege"),
                new Appointment("Noah", "Fatima", LocalDateTime.now().plusDays(3).withHour(10).withMinute(0), "Klassischer Schnitt")
        );
        model.addAttribute("confirmationText", templateService.buildConfirmation(sampleAppointment));
        model.addAttribute("overview", templateService.buildWeeklyOverview(nextAppointments));
        model.addAttribute("showcase", templateService.buildShowcase(sampleAppointment));
        return "templates";
    }
}
