# Musterlösung: Java Streams

## MovieRatingsService.java

```java
import java.time.Duration;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public class MovieRatingsService {

    private final MovieRatingsDataset ds;

    public MovieRatingsService(MovieRatingsDataset ds) {
        this.ds = ds;
    }

    public Stream<MovieRating> getRatings(long limit) {
        return ds.load().limit(limit);
    }

    public Stream<MovieRating> getRatingsSortedByVotes(boolean ascending) {
        Comparator<MovieRating> comparator = Comparator.comparing(MovieRating::votes);
        if (!ascending) {
            comparator = comparator.reversed();
        }
        return ds.load().sorted(comparator);
    }

    public Stream<MovieRating> getRatingsGreaterThan(double cutoffInclusive) {
        return ds.load().filter(m -> m.value() >= cutoffInclusive);
    }

    public Stream<String> getNamesSortedAlphabetically() {
        return ds.load()
                .map(MovieRating::name)
                .sorted();
    }

    public Stream<String> getDistinctGenres() {
        return ds.load()
                .flatMap(m -> m.genres().stream())
                .distinct();
    }

    public Stream<MovieRating> getMoviesHavingAllGenres(Set<String> searchGenres) {
        return ds.load()
                .filter(m -> m.genres().containsAll(searchGenres));
    }

    public Long getTotalVoteCountOverAllRatings() {
        return ds.load()
                .mapToLong(MovieRating::votes)
                .sum();
    }

    public Stream<MovieRating> getMovieRatingsSortedByDuration(boolean ascending) {
        Comparator<MovieRating> comparator = Comparator.comparing(MovieRating::runtime);
        if (!ascending) {
            comparator = comparator.reversed();
        }
        return ds.load().sorted(comparator);
    }

    public Float getAverageRatingOfMoviesHavingGenre(String genre) {
        return (float) ds.load()
                .filter(m -> m.genres().contains(genre))
                .mapToDouble(MovieRating::value)
                .average()
                .orElse(0.0);
    }

    public Optional<MovieRating> findMovieWithName(String name) {
        return ds.load()
                .filter(m -> m.name().equals(name))
                .findAny();
    }

    public Stream<MovieRating> getMoviesWithDurationLessThan(Duration duration) {
        return ds.load()
                .filter(m -> m.runtime().compareTo(duration) < 0);
    }

    // Bonus: Generische Sortierung
    public <T extends Comparable<T>> Stream<MovieRating> getRatingsSortedByField(
            Function<MovieRating, T> keyExtractor, boolean ascending) {
        
        Comparator<MovieRating> comparator = Comparator.comparing(keyExtractor);
        if (!ascending) {
            comparator = comparator.reversed();
        }
        return ds.load().sorted(comparator);
    }
}
```
