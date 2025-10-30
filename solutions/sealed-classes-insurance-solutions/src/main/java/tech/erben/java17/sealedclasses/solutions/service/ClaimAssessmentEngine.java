package tech.erben.java17.sealedclasses.solutions.service;

import tech.erben.java17.sealedclasses.solutions.model.Assessment;
import tech.erben.java17.sealedclasses.solutions.model.Assessment.ApprovedAssessment;
import tech.erben.java17.sealedclasses.solutions.model.Assessment.DeclinedAssessment;
import tech.erben.java17.sealedclasses.solutions.model.Assessment.ManualReviewAssessment;
import tech.erben.java17.sealedclasses.solutions.model.Claim;
import tech.erben.java17.sealedclasses.solutions.model.HomeClaim;
import tech.erben.java17.sealedclasses.solutions.model.HealthClaim;
import tech.erben.java17.sealedclasses.solutions.model.MotorClaim;
import tech.erben.java17.sealedclasses.solutions.model.TravelClaim;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ClaimAssessmentEngine {

    public Assessment assess(Claim claim) {
        if (claim instanceof MotorClaim motor) {
            return handleMotorClaim(motor);
        } else if (claim instanceof TravelClaim travel) {
            return handleTravelClaim(travel);
        } else if (claim instanceof HomeClaim home) {
            return handleHomeClaim(home);
        } else if (claim instanceof HealthClaim health) {
            return handleHealthClaim(health);
        }
        throw new IllegalStateException("Unbekannter Claim-Typ: " + claim.getClass().getName());
    }

    public String nextStep(Assessment assessment) {
        if (assessment instanceof ApprovedAssessment approved) {
            return "Auszahlung vorbereiten: " + approved.payout() + " €";
        } else if (assessment instanceof ManualReviewAssessment manual) {
            return "Weiterleiten an Spezialteam \"" + manual.specialistTeam() + "\"";
        } else if (assessment instanceof DeclinedAssessment declined) {
            return "Fall schließen (Code: " + declined.reasonCode() + ")";
        }
        throw new IllegalStateException("Unbekannter Assessment-Typ: " + assessment.getClass().getName());
    }

    private Assessment handleMotorClaim(MotorClaim claim) {
        if (claim.severity() == MotorClaim.DamageSeverity.MINOR && claim.drivable()) {
            var payout = claim.amount().min(new BigDecimal("2000"));
            var summary = "Schnellregulierung Kfz (Police %s)".formatted(claim.policyNumber());
            return new ApprovedAssessment(summary, payout);
        }
        if (!claim.drivable()) {
            return new ManualReviewAssessment("Gutachten erforderlich: Fahrzeug nicht fahrbereit (" + claim.damageDescription() + ")",
                    "Kfz-Spezial");
        }
        return new ManualReviewAssessment("Zusatzprüfung für mittleren Schaden anfordern", "Kfz-Standard");
    }

    private Assessment handleTravelClaim(TravelClaim claim) {
        return switch (claim.incidentType()) {
            case DELAY -> new ApprovedAssessment("Flugverspätung in %s".formatted(claim.destinationCountry()),
                    claim.amount().min(new BigDecimal("500")));
            case MEDICAL -> {
                if (!claim.documentationComplete()) {
                    yield new ManualReviewAssessment("Medizinische Unterlagen anfordern", "Reise-Medical");
                }
                yield new ApprovedAssessment("Medizinischer Zwischenfall in %s".formatted(claim.destinationCountry()),
                        claim.amount());
            }
            case LOSS -> new ManualReviewAssessment("Diebstahl/Verlust prüfen (Unterlagen ergänzen)",
                    "Reise-Diebstahl");
        };
    }

    private Assessment handleHomeClaim(HomeClaim claim) {
        return switch (claim.cause()) {
            case WATER -> new ManualReviewAssessment("Sanitär-Notdienst prüfen für " + claim.propertyType(),
                    "Hausrat-Wasser");
            case FIRE -> new ManualReviewAssessment("Brandschaden: Feuerwehrbericht abwarten", "Hausrat-Brand");
            case STORM -> {
                var summary = "Sturmschaden " + claim.propertyType() + " in Prüfung";
                yield new ApprovedAssessment(summary, claim.amount().min(new BigDecimal("10000")));
            }
            case THEFT -> new DeclinedAssessment("Diebstahlschaden unzureichend belegt", "HOME-DECL-THF");
        };
    }

    private Assessment handleHealthClaim(HealthClaim claim) {
        var daysSinceReport = ChronoUnit.DAYS.between(claim.reportedAt(), java.time.LocalDate.now());
        if (daysSinceReport > 30) {
            return new DeclinedAssessment("Fristüberschreitung bei Gesundheitsfall", "HEALTH-LATE");
        }
        if (claim.inpatient()) {
            return new ManualReviewAssessment("Stationärer Aufenthalt für Diagnose " + claim.diagnosis(),
                    "Medical-Team");
        }
        var coverage = switch (claim.treatmentType()) {
            case SURGERY -> claim.amount();
            case REHAB -> claim.amount().min(new BigDecimal("4000"));
            case OUTPATIENT -> claim.amount().min(new BigDecimal("750"));
        };
        return new ApprovedAssessment("Ambulanter Gesundheitsfall: " + claim.diagnosis(), coverage);
    }

    public List<Assessment> bulkAssess(List<Claim> claims) {
        return claims.stream()
                .map(this::assess)
                .toList();
    }
}
