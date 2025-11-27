package tech.erben.java17.streams.movies;
import java.time.Duration;
import java.util.Comparator;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        MovieRatingsService service = new MovieRatingsService(CsvBackedMovieRatingsDataset.getInstance());

        System.out.println("Top 5 Drama-Filme nach Stimmen:");
        service
            .getMoviesHavingAllGenres(Set.of("Drama"))
            .sorted(Comparator.comparing(MovieRating::votes).reversed())
            .limit(5)
            .forEach(Main::printMovie);

        System.out.println("\nKurze Watchlist (< 95 Minuten), nach Bewertung:");
        service
            .getMoviesWithDurationLessThan(Duration.ofMinutes(95))
            .sorted(Comparator.comparing(MovieRating::value).reversed())
            .limit(5)
            .forEach(Main::printMovie);

        System.out.printf("%nDurchschnittliche Sci-Fi-Bewertung: %.2f%n", service.getAverageRatingOfMoviesHavingGenre("Sci-Fi"));
        System.out.printf("Genres (erste 8): %s%n", service.getDistinctGenres().sorted().limit(8).toList());

        System.out.println("\nAlphabetische Stichprobe (3 Einträge):");
        service
            .getRatingsSortedByField(MovieRating::name, true)
            .limit(3)
            .forEach(Main::printMovie);
    }

    private static void printMovie(MovieRating movie) {
        System.out.printf(
            "- %s (%.1f Punkte, %d Stimmen, %s)%n",
            movie.name(),
            movie.value(),
            movie.votes(),
            movie.runtime()
        );
    }
}
