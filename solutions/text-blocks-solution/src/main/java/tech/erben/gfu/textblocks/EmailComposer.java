package tech.erben.gfu.textblocks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EmailComposer {

    private static final DateTimeFormatter START_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public String buildWelcomeMail(String recipientName, String workshopTitle, String joinLink) {
        return """
                <html>
                  <body>
                    <h1>Willkommen, %s!</h1>
                    <p>Du bist erfolgreich für "%s" registriert.</p>
                    <p>Bitte nutze folgenden Link für den Beitritt: <a href="%s">Workshop öffnen</a></p>
                    <p>Viel Spaß!</p>
                  </body>
                </html>
                """
                .stripIndent()
                .formatted(recipientName, workshopTitle, joinLink);
    }

    public String buildReminderMail(String recipientName, String workshopTitle, LocalDateTime startsAt) {
        String formattedStart = START_TIME_FORMATTER.format(startsAt);

        return """
                Hallo %s,

                dies ist eine kurze Erinnerung an deinen Kurs "%s".
                Startzeit: %s
                Falls du Fragen hast, antworte einfach auf diese Mail.

                Bis bald,
                Dein GFU-Team
                """
                .stripIndent()
                .formatted(recipientName, workshopTitle, formattedStart);
    }
}
