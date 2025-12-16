package tech.erben.gfu.textblocks;

import tech.erben.gfu.textblocks.legacy.EmailComposer;
import tech.erben.gfu.textblocks.legacy.MarkdownRenderer;
import tech.erben.gfu.textblocks.legacy.SqlTemplates;

import java.time.LocalDateTime;
import java.util.List;

public class TextBlockPlayground {
    public static void main(String[] args) {
        EmailComposer composer = new EmailComposer();
        String welcome = composer.buildWelcomeMail("Rita", "Java 21 Text Blocks", "https://example.com/join/123");
        String reminder = composer.buildReminderMail("Rita", "Java 21 Text Blocks", LocalDateTime.now().plusDays(1));

        SqlTemplates sqlTemplates = new SqlTemplates();
        String selectSql = sqlTemplates.selectActiveRegistrations();
        String insertSql = sqlTemplates.insertAuditLog();
        String filteredSql = sqlTemplates.selectRegistrationsByIds(3);

        MarkdownRenderer markdownRenderer = new MarkdownRenderer();
        String releaseNotes = markdownRenderer.renderReleaseNotes("1.2.0", List.of("Neue Text-Block-Übung", "SQL-Templates aufgeräumt", "Markdown-Renderer entschlackt"));
        String apiDoc = markdownRenderer.renderApiDoc("POST /registrations", "Erzeugt eine neue Registrierung", List.of("name", "email", "workshopId"));

        System.out.println("=== Welcome Mail ===");
        System.out.println(welcome);
        System.out.println("\n=== Reminder Mail ===");
        System.out.println(reminder);

        System.out.println("\n=== SQL Queries ===");
        System.out.println(selectSql);
        System.out.println();
        System.out.println(insertSql);
        System.out.println();
        System.out.println(filteredSql);

        System.out.println("\n=== Markdown Release Notes ===");
        System.out.println(releaseNotes);
        System.out.println("\n=== Markdown API Doc ===");
        System.out.println(apiDoc);
    }
}
