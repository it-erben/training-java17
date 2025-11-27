package tech.erben.gfu.jigsaw.greetings;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Kleine API, die von einem anderen Modul via JPMS genutzt wird.
 */
public class GreetingService {

    private final String salutation;

    public GreetingService(String salutation) {
        this.salutation = Objects.requireNonNull(salutation, "salutation");
    }

    public static GreetingService german() {
        return new GreetingService("Hallo");
    }

    public static GreetingService english() {
        return new GreetingService("Hello");
    }

    public String greet(String name) {
        String target = (name == null || name.isBlank()) ? "World" : name.trim();
        return "%s, %s!".formatted(salutation, target);
    }

    public String greetWithTimestamp(String name, LocalTime time) {
        String timeString = time.format(DateTimeFormatter.ofPattern("HH:mm"));
        return "%s %s (%s)".formatted(greet(name), timeString, timeZoneHint());
    }

    private String timeZoneHint() {
        return "System TZ";
    }
}
