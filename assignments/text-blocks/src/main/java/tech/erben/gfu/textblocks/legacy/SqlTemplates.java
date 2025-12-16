package tech.erben.gfu.textblocks.legacy;

import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

public class SqlTemplates {

    public String selectActiveRegistrations() {
        return "SELECT r.id, r.first_name, r.last_name, r.email, w.title\n" +
                "FROM registrations r\n" +
                "JOIN workshops w ON w.id = r.workshop_id\n" +
                "WHERE r.active = TRUE\n" +
                "ORDER BY w.title, r.last_name";
    }

    public String selectRegistrationsByIds(int placeholderCount) {
        List<String> placeholders = Collections.nCopies(placeholderCount, "?");
        StringJoiner joiner = new StringJoiner(", ");
        placeholders.forEach(joiner::add);

        return "SELECT r.id, r.email, r.active\n" +
                "FROM registrations r\n" +
                "WHERE r.id IN (" + joiner + ")\n" +
                "  AND r.active = TRUE";
    }

    public String insertAuditLog() {
        return "INSERT INTO audit_log (subject, action, metadata, created_at)\n" +
                "VALUES (?, ?, ?, NOW())";
    }
}
