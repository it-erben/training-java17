package tech.erben.java17.textblocks.service;

import tech.erben.java17.textblocks.model.Appointment;
import tech.erben.java17.textblocks.model.TextBlockShowcase;
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

    public TextBlockShowcase buildShowcase(Appointment referenceAppointment) {
        var classicList = "Haarschnitt\nBartpflege\nKopfmassage";
        var textBlockList = """
                Haarschnitt
                Bartpflege
                Kopfmassage
                """.stripTrailing();
        var rawSql = """
                SELECT a.customer_name,
                       a.slot,
                       a.treatment
                  FROM appointments a
                 WHERE a.barber_name = '%s'
                 ORDER BY a.slot
                """.formatted(referenceAppointment.barberName());
        var normalizedSql = rawSql.stripIndent();

        var jsonPayload = """
                {
                  "customer": "%s",
                  "barber": "%s",
                  "slot": "%s",
                  "treatment": "%s"
                }
                """.formatted(
                referenceAppointment.customerName(),
                referenceAppointment.barberName(),
                DATE_TIME_FORMATTER.format(referenceAppointment.slot()),
                referenceAppointment.treatment())
                .stripIndent();

        var htmlSnippet = """
                <article class="appointment">
                    <h2>%s – %s</h2>
                    <p>Termin am %s bei %s.</p>
                </article>
                """.formatted(
                referenceAppointment.customerName(),
                referenceAppointment.treatment(),
                DATE_TIME_FORMATTER.format(referenceAppointment.slot()),
                referenceAppointment.barberName())
                .stripIndent();

        var rawEscapes = """
                Kontakt: \\t%s
                Hinweis: \\u00A9 Cut & Shave - alle Rechte vorbehalten.
                """.formatted(referenceAppointment.customerName()).stripIndent();
        var translatedEscapes = rawEscapes.translateEscapes();

        var indentedMenu = """
                Top-Leistungen:
                - Deluxe Fade
                - Bartpflege
                - Treatment
                """.indent(4).stripTrailing();

        var priceTableTemplate = """
                | %-15s | %-8s |
                | %-15s | %-8s |
                | %-15s | %-8s |
                """;
        var formattedTable = priceTableTemplate.stripIndent().formatted(
                "Leistung", "Preis",
                "Deluxe Fade", "29 €",
                "Bartpflege", "19 €"
        );

        return new TextBlockShowcase(
                classicList,
                textBlockList,
                classicList.equals(textBlockList),
                rawSql,
                normalizedSql,
                jsonPayload,
                htmlSnippet,
                rawEscapes,
                translatedEscapes,
                indentedMenu,
                formattedTable
        );
    }
}
