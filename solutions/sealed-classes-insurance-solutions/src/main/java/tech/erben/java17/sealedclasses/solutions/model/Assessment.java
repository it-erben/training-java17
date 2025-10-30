package tech.erben.java17.sealedclasses.solutions.model;

import java.math.BigDecimal;

public sealed interface Assessment permits Assessment.ApprovedAssessment,
        Assessment.ManualReviewAssessment,
        Assessment.DeclinedAssessment {
    String summary();

    record ApprovedAssessment(String summary, BigDecimal payout) implements Assessment {
        public ApprovedAssessment {
            if (payout == null || payout.signum() < 0) {
                throw new IllegalArgumentException("payout darf nicht negativ sein");
            }
        }
    }

    record ManualReviewAssessment(String summary, String specialistTeam) implements Assessment {
        public ManualReviewAssessment {
            if (specialistTeam == null || specialistTeam.isBlank()) {
                throw new IllegalArgumentException("specialistTeam darf nicht leer sein");
            }
        }
    }

    record DeclinedAssessment(String summary, String reasonCode) implements Assessment {
        public DeclinedAssessment {
            if (reasonCode == null || reasonCode.isBlank()) {
                throw new IllegalArgumentException("reasonCode darf nicht leer sein");
            }
        }
    }
}
