package tech.erben.java17.sealedclasses.web;

import tech.erben.java17.sealedclasses.model.CarClaim;
import tech.erben.java17.sealedclasses.model.Claim;
import tech.erben.java17.sealedclasses.model.HealthClaim;
import tech.erben.java17.sealedclasses.model.TravelClaim;
import tech.erben.java17.sealedclasses.service.ClaimAssessmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class ClaimController {

    private final ClaimAssessmentService assessmentService;

    public ClaimController(ClaimAssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    @GetMapping("/")
    public String showClaims(Model model) {
        List<Claim> claims = List.of(
                new CarClaim("KFZ-123", new BigDecimal("2200"), "Heckschaden nach Auffahrunfall", false),
                new HealthClaim("MED-456", new BigDecimal("780"), "Bänderriss", false),
                new TravelClaim("TRV-789", new BigDecimal("1500"), "Japan", true)
        );
        model.addAttribute("claims", claims);
        model.addAttribute("assessmentService", assessmentService);
        return "claims";
    }
}
