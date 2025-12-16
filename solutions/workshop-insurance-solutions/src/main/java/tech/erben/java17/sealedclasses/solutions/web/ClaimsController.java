package tech.erben.java17.sealedclasses.solutions.web;

import tech.erben.java17.sealedclasses.solutions.service.ClaimAssessmentEngine;
import tech.erben.java17.sealedclasses.solutions.service.ClaimShowcaseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClaimsController {

    private final ClaimShowcaseService showcaseService;
    private final ClaimAssessmentEngine assessmentEngine;

    public ClaimsController(ClaimShowcaseService showcaseService, ClaimAssessmentEngine assessmentEngine) {
        this.showcaseService = showcaseService;
        this.assessmentEngine = assessmentEngine;
    }

    @GetMapping("/")
    public String show(Model model) {
        var claims = showcaseService.claims();
        var assessments = assessmentEngine.bulkAssess(claims);

        model.addAttribute("claims", claims);
        model.addAttribute("assessments", assessments);
        model.addAttribute("engine", assessmentEngine);
        return "claims";
    }
}
