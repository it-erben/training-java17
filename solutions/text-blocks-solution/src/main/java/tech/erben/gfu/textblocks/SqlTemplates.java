package tech.erben.gfu.textblocks;

import java.util.Collections;

public class SqlTemplates {

    public String selectActiveRegistrations() {
        return """
                SELECT r.id, r.first_name, r.last_name, r.email, w.title
                FROM registrations r
                JOIN workshops w ON w.id = r.workshop_id
                WHERE r.active = TRUE
                  /* copyable query:
                     keep \"?\" placeholders and \\-escapes intact */
                ORDER BY w.title, r.last_name
                """.stripIndent();
    }

    public String selectRegistrationsByIds(int placeholderCount) {
        if (placeholderCount <= 0) {
            throw new IllegalArgumentException("placeholderCount must be positive");
        }
        String placeholders = String.join(", ", Collections.nCopies(placeholderCount, "?"));

        return """
                SELECT r.id, r.email, r.active
                FROM registrations r
                WHERE r.id IN (%s)
                  AND r.active = TRUE
                """.stripIndent().formatted(placeholders);
    }

    public String insertAuditLog() {
        return """
                INSERT INTO audit_log (subject, action, metadata, created_at)
                VALUES (?, ?, ?, NOW())
                """.stripIndent();
    }
}
