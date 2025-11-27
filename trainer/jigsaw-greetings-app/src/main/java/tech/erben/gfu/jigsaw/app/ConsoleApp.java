package tech.erben.gfu.jigsaw.app;

import tech.erben.gfu.jigsaw.greetings.GreetingService;

import java.time.LocalTime;
import java.util.Arrays;

public class ConsoleApp {

    public static void main(String[] args) {
        GreetingService service = GreetingService.german();
        String name = args.length > 0 ? String.join(" ", args) : "Jigsaw";

        System.out.println(service.greetWithTimestamp(name, LocalTime.now()));
        System.out.println("Available salutations: " + Arrays.toString(new String[]{"German", "English"}));
    }
}
