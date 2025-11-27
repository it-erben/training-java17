package tech.erben.java17.streams.movies;

import java.time.Duration;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MovieRatingsService {

    private final MovieRatingsDataset ds;

    public MovieRatingsService(MovieRatingsDataset ds) {
        this.ds = ds;
    }

    /**
     * Retrieves a stream of movie ratings with a limit.
     *
     * @param limit The maximum number of movie ratings to retrieve.
     * @return A stream of MovieRating objects representing the movie ratings.
     */
    public Stream<MovieRating> getRatings(long limit) {
        return ds.load().limit(limit);
    }

    /**
     * Retrieves a stream of movie ratings sorted by votes.
     *
     * @param ascending Determines whether the ratings should be sorted in ascending order.
     * @return A stream of MovieRating objects representing the movie ratings sorted by votes.
     */
    public Stream<MovieRating> getRatingsSortedByVotes(boolean ascending) {
        Comparator<MovieRating> comparator = Comparator.comparing(
            MovieRating::votes,
            Comparator.nullsLast(Integer::compareTo)
        );
        return ds.load().sorted(ascending ? comparator : comparator.reversed());
    }

    /**
     * Retrieves a stream of movie ratings greater than or equal to the specified cutoff.
     *
     * @param cutoffInclusive The minimum rating cutoff (inclusive).
     * @return A stream of MovieRating objects representing the movie ratings greater than or equal to the cutoff.
     */
    public Stream<MovieRating> getRatingsGreaterThan(double cutoffInclusive) {
        return ds
            .load()
            .filter(movie -> Optional.ofNullable(movie.value()).orElse(0f) >= cutoffInclusive);
    }

    /**
     * Retrieves a stream of names sorted alphabetically.
     *
     * @return A stream of strings representing the names sorted alphabetically.
     */
    public Stream<String> getNamesSortedAlphabetically() {
        return ds
            .load()
            .map(MovieRating::name)
            .filter(Objects::nonNull)
            .sorted(String.CASE_INSENSITIVE_ORDER);
    }

    /**
     * Retrieves a stream of distinct genres from the movie ratings dataset.
     *
     * @return A stream of strings representing the distinct genres.
     */
    public Stream<String> getDistinctGenres() {
        return ds
            .load()
            .flatMap(movie -> movie.genres().stream())
            .map(String::strip)
            .filter(genre -> !genre.isBlank())
            .distinct()
            .sorted(String.CASE_INSENSITIVE_ORDER);
    }

    /**
     * Retrieves a stream of movie ratings that have all the specified genres.
     *
     * @param genre A set of strings representing the genres to filter by.
     * @return A stream of MovieRating objects representing the movie ratings that have all the specified genres.
     */
    public Stream<MovieRating> getMoviesHavingAllGenres(Set<String> genre) {
        Set<String> requiredGenres = Optional
            .ofNullable(genre)
            .orElse(Set.of())
            .stream()
            .map(String::strip)
            .filter(s -> !s.isBlank())
            .collect(Collectors.toUnmodifiableSet());

        if (requiredGenres.isEmpty()) {
            return ds.load();
        }

        return ds
            .load()
            .filter(movie -> movie.genres().containsAll(requiredGenres));
    }

    /**
     * Returns the total vote count over all movie ratings.
     *
     * @return The total vote count over all movie ratings.
     */
    public Long getTotalVoteCountOverAllRatings() {
        return ds.load().mapToLong(movie -> Optional.ofNullable(movie.votes()).orElse(0)).sum();
    }

    /**
     * Retrieves a stream of movie ratings sorted by duration.
     *
     * @param ascending Determines whether the ratings should be sorted in ascending order.
     * @return A stream of MovieRating objects representing the movie ratings sorted by duration.
     */
    public Stream<MovieRating> getMovieRatingsSortedByDuration(boolean ascending) {
        Comparator<MovieRating> comparator = Comparator.comparing(
            MovieRating::runtime,
            Comparator.nullsLast(Duration::compareTo)
        );
        return ds.load().sorted(ascending ? comparator : comparator.reversed());
    }

    /**
     * Returns the average rating of movies having the specified genre.
     *
     * @param genre The genre of movies to filter by.
     * @return The average rating of movies having the specified genre.
     */
    public Float getAverageRatingOfMoviesHavingGenre(String genre) {
        double average = ds
            .load()
            .filter(movie -> movie.genres().contains(genre))
            .mapToDouble(movie -> Optional.ofNullable(movie.value()).orElse(0f))
            .average()
            .orElse(Double.NaN);

        return (float) average;
    }

    /**
     * Finds a movie rating by its name.
     *
     * @param name The name of the movie.
     * @return An optional MovieRating object representing the rating of the movie with the specified name,
     * or an empty optional if no movie with the given name is found.
     */
    public Optional<MovieRating> findMovieWithName(String name) {
        return ds
            .load()
            .filter(movie -> movie.name() != null)
            .filter(movie -> movie.name().equalsIgnoreCase(name))
            .findFirst();
    }

    /**
     * Retrieves a stream of movie ratings with a duration less than the specified duration.
     *
     * @param duration The maximum duration of the movie ratings to retrieve.
     * @return A stream of MovieRating objects representing the movie ratings with a duration less than the specified duration.
     */
    public Stream<MovieRating> getMoviesWithDurationLessThan(Duration duration) {
        return ds
            .load()
            .filter(movie -> movie.runtime() != null)
            .filter(movie -> movie.runtime().compareTo(duration) < 0);
    }

    /**
     * Bonus: Sort the ratings by an arbitrary comparable field.
     * @param fieldExtractor accessor for the sortable field
     * @param ascending true for ascending, false for descending
     * @return sorted stream of ratings
     * @param <T> comparable field type
     */
    public <T extends Comparable<? super T>> Stream<MovieRating> getRatingsSortedByField(
        Function<MovieRating, T> fieldExtractor,
        boolean ascending
    ) {
        return getRatingsSortedByField(ds.load(), fieldExtractor, ascending);
    }

    public <T extends Comparable<? super T>> Stream<MovieRating> getRatingsSortedByField(
        Stream<MovieRating> source,
        Function<MovieRating, T> fieldExtractor,
        boolean ascending
    ) {
        Comparator<MovieRating> comparator = Comparator.comparing(
            fieldExtractor,
            Comparator.nullsLast(Comparator.naturalOrder())
        );
        return source.sorted(ascending ? comparator : comparator.reversed());
    }
}
