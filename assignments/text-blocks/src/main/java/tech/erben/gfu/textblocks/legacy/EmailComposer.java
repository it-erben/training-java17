package tech.erben.gfu.textblocks.legacy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EmailComposer {

    public String buildWelcomeMail(String recipientName, String workshopTitle, String joinLink) {
        return "<html>\n" +
                "<body>\n" +
                "  <h1>Willkommen, " + recipientName + "!</h1>\n" +
                "  <p>Du bist erfolgreich für \"" + workshopTitle + "\" registriert.</p>\n" +
                "  <p>Bitte nutze folgenden Link für den Beitritt: <a href=\"" + joinLink + "\">Workshop öffnen</a></p>\n" +
                "  <p>Viel Spaß!</p>\n" +
                "</body>\n" +
                "</html>";
    }

    public String buildReminderMail(String recipientName, String workshopTitle, LocalDateTime startsAt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        String greeting = "Hallo " + recipientName + ",";
        String body = "dies ist eine kurze Erinnerung an deinen Kurs \"" + workshopTitle + "\".\n" +
                "Startzeit: " + formatter.format(startsAt) + "\n" +
                "Falls du Fragen hast, antworte einfach auf diese Mail.";
        String footer = "Bis bald,\n" +
                "Dein GFU-Team";

        return greeting + "\n\n" + body + "\n\n" + footer;
    }
}
