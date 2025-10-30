package tech.erben.java17.sealedclasses.solutions.service;

import tech.erben.java17.sealedclasses.solutions.model.*;
import tech.erben.java17.sealedclasses.solutions.model.HealthClaim.TreatmentType;
import tech.erben.java17.sealedclasses.solutions.model.HomeClaim.DamageCause;
import tech.erben.java17.sealedclasses.solutions.model.MotorClaim.DamageSeverity;
import tech.erben.java17.sealedclasses.solutions.model.TravelClaim.IncidentType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ClaimShowcaseService {

    private final List<Claim> sampleClaims = List.of(
            new MotorClaim("KFZ-900", new BigDecimal("1800"), LocalDate.now().minusDays(2),
                    DamageSeverity.MINOR, true, "Frontschaden am Nummernschild"),
            new MotorClaim("KFZ-901", new BigDecimal("7800"), LocalDate.now().minusDays(1),
                    DamageSeverity.MAJOR, false, "Heckschaden mit Rahmentreffer"),
            new TravelClaim("TRV-222", new BigDecimal("450"), LocalDate.now().minusDays(4),
                    "Japan", IncidentType.DELAY, true),
            new TravelClaim("TRV-333", new BigDecimal("2300"), LocalDate.now().minusDays(3),
                    "Kanada", IncidentType.MEDICAL, false),
            new HomeClaim("HME-110", new BigDecimal("5600"), LocalDate.now().minusDays(6),
                    "Einfamilienhaus", DamageCause.STORM, false),
            new HomeClaim("HME-111", new BigDecimal("3200"), LocalDate.now().minusDays(8),
                    "Mietwohnung", DamageCause.THEFT, false),
            new HealthClaim("MED-777", new BigDecimal("1200"), LocalDate.now().minusDays(12),
                    "Meniskusriss", TreatmentType.REHAB, false),
            new HealthClaim("MED-778", new BigDecimal("820"), LocalDate.now().minusDays(45),
                    "Physiotherapie", TreatmentType.OUTPATIENT, false)
    );

    public List<Claim> claims() {
        return sampleClaims;
    }
}
