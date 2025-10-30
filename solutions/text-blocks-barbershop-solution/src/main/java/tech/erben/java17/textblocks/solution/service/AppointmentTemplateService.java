package tech.erben.java17.textblocks.solution.service;

import tech.erben.java17.textblocks.solution.model.Appointment;
import tech.erben.java17.textblocks.solution.model.TemplateVariant;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class AppointmentTemplateService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("EEEE, dd.MM.yyyy HH:mm", Locale.GERMAN);

    public String buildConfirmation(Appointment appointment) {
        var template = """
                Liebe%s %s,

                dein Termin bei %s für "%s" ist bestätigt.
                Bitte erscheine mindestens 10 Minuten vor Beginn.

                Wir freuen uns auf dich!
                Dein Cut & Shave Team
                """;
        var salutationSuffix = appointment.customerName().endsWith("a") ? "" : "r";
        return template.formatted(salutationSuffix, appointment.customerName(),
                appointment.barberName(), appointment.treatment()).stripTrailing();
    }

    public String buildWeeklyOverview(List<Appointment> appointments) {
        var header = """
                Cut & Shave Kalenderwoche
                -------------------------
                """;
        var entries = appointments.stream()
                .map(appointment -> "%s – %s bei %s (%s)".formatted(
                        DATE_TIME_FORMATTER.format(appointment.slot()),
                        appointment.customerName(),
                        appointment.barberName(),
                        appointment.treatment()))
                .collect(Collectors.joining("\n"));
        return (header + entries).stripTrailing();
    }

    public String buildHtmlEmail(Appointment appointment) {
        return """
                <html>
                  <body>
                    <h2>Terminbestätigung</h2>
                    <p>Liebe%s %s,</p>
                    <p>dein Termin bei <strong>%s</strong> für <em>%s</em> ist bestätigt.</p>
                    <p><strong>Termin:</strong> %s</p>
                    <p>Wir freuen uns auf dich!</p>
                    <p>Dein Cut &amp; Shave Team</p>
                  </body>
                </html>
                """.formatted(
                appointment.customerName().endsWith("a") ? "" : "r",
                appointment.customerName(),
                appointment.barberName(),
                appointment.treatment(),
                DATE_TIME_FORMATTER.format(appointment.slot())
        ).stripIndent();
    }

    public TemplateVariant buildMarkdownPreview(Appointment appointment) {
        var markdown = """
                ## Terminübersicht
                - Kund:in: %s
                - Barber: %s
                - Wann: %s
                - Behandlung: %s
                """.formatted(
                appointment.customerName(),
                appointment.barberName(),
                DATE_TIME_FORMATTER.format(appointment.slot()),
                appointment.treatment()
        ).stripIndent();

        return new TemplateVariant("Markdown-Vorschau", markdown);
    }

    public TemplateVariant buildIndentedPromotion() {
        var promotion = """
                Herbst-Special:
                    - Deluxe Fade + Bartpflege
                    - 10%% Rabatt auf Styling-Produkte
                    - Warmes Handtuch inklusive
                """.indent(2).stripTrailing();
        return new TemplateVariant("Herbstaktion", promotion);
    }

    public TemplateVariant buildEscapedSnippet() {
        var raw = """
                Kontakt: \tservice@cut-shave.example
                Promo-Code: \u20AC-SHAVE
                """;
        return new TemplateVariant("Escape-Sequenzen", raw.translateEscapes());
    }

    public TemplateVariant buildSqlSnippet(String barberName) {
        var sql = """
                SELECT slot, treatment
                FROM appointments
                WHERE barber = '%s'
                ORDER BY slot
                """.formatted(barberName).stripIndent();
        var prettified = sql.stripIndent();
        return new TemplateVariant("SQL-Export", prettified);
    }
}
