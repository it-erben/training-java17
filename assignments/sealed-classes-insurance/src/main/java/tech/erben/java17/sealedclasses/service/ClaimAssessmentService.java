package tech.erben.java17.sealedclasses.service;

import tech.erben.java17.sealedclasses.model.CarClaim;
import tech.erben.java17.sealedclasses.model.Claim;
import tech.erben.java17.sealedclasses.model.HealthClaim;
import tech.erben.java17.sealedclasses.model.TravelClaim;
import org.springframework.stereotype.Service;

@Service
public class ClaimAssessmentService {

    public String assess(Claim claim) {
        if (claim instanceof CarClaim car) {
            var severity = car.drivable() ? "mittleren" : "schweren";
            return "Kfz-Schaden (%s) mit %s Schäden.".formatted(car.damageDescription(), severity);
        } else if (claim instanceof HealthClaim health) {
            return health.inpatient()
                    ? "Gesundheitsfall stationär, medizinische Prüfung eskalieren."
                    : "Ambulanter Gesundheitsfall, automatisierte Prüfung starten.";
        } else if (claim instanceof TravelClaim travel) {
            return travel.documentationComplete()
                    ? "Reiseschaden in %s, Auszahlung vorbereiten.".formatted(travel.incidentCountry())
                    : "Reiseschaden in %s, Dokumente nachfordern.".formatted(travel.incidentCountry());
        }
        throw new IllegalStateException("Unbekannter Claim-Typ: " + claim.getClass().getName());
    }

    public String nextStep(Claim claim) {
        if (claim instanceof CarClaim car) {
            return car.drivable() ? "Gutachter:in optional anfragen." : "Sofort Gutachter:in zuweisen.";
        } else if (claim instanceof HealthClaim health) {
            return "Medizinisches Team informieren (Diagnose: %s)".formatted(health.diagnosis());
        } else if (claim instanceof TravelClaim travel) {
            return travel.documentationComplete() ? "Kontakt zum Schadenservice halten." : "Kund:in um Belege bitten.";
        }
        throw new IllegalStateException("Unbekannter Claim-Typ: " + claim.getClass().getName());
    }
}
