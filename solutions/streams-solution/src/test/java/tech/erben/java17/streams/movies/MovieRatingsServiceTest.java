package tech.erben.java17.streams.movies;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MovieRatingsServiceTest {

    private MovieRatingsService serviceWith(MovieRating... ratings) {
        return new MovieRatingsService(new InMemoryMovieDataset(List.of(ratings)));
    }

    @Test
    void testGetRatingsWithLimit() {
        MovieRatingsService service = serviceWith(
            new MovieRating(1, "Movie 1", 8.5f, 120, Duration.ofMinutes(90), Set.of("Action"), "Description 1"),
            new MovieRating(2, "Movie 2", 7.5f, 100, Duration.ofMinutes(120), Set.of("Drama"), "Description 2")
        );

        assertEquals(2, service.getRatings(2).count(), "Should return all movie ratings when limit is equal to dataset size");
        assertEquals(1, service.getRatings(1).count(), "Should return limited movie ratings when limit is less than dataset size");
    }

    @Test
    void testGetRatingsSortedByVotes() {
        MovieRatingsService service = serviceWith(
            new MovieRating(1, "Movie 1", 8.5f, 100, Duration.ofMinutes(90), Set.of("Action"), "Description 1"),
            new MovieRating(2, "Movie 2", 7.5f, 120, Duration.ofMinutes(120), Set.of("Drama"), "Description 2")
        );

        List<MovieRating> sortedAscending = service.getRatingsSortedByVotes(true).toList();
        assertTrue(sortedAscending.get(0).votes() <= sortedAscending.get(1).votes(), "Should be sorted in ascending order by votes");

        List<MovieRating> sortedDescending = service.getRatingsSortedByVotes(false).toList();
        assertTrue(sortedDescending.get(0).votes() >= sortedDescending.get(1).votes(), "Should be sorted in descending order by votes");
    }

    @Test
    void testGenericSortingByField() {
        MovieRatingsService service = serviceWith(
            new MovieRating(1, "B-Movie", 7.0f, 10, Duration.ofMinutes(90), Set.of("Drama"), "B first alphabetically"),
            new MovieRating(2, "A-Movie", 9.0f, 5, Duration.ofMinutes(100), Set.of("Comedy"), "Highest rating"),
            new MovieRating(3, "A-Movie", 8.0f, 15, Duration.ofMinutes(80), Set.of("Action"), "Second highest")
        );

        List<MovieRating> sortedByName = service.getRatingsSortedByField(MovieRating::name, true).toList();
        assertEquals(
            List.of(
                new MovieRating(2, "A-Movie", 9.0f, 5, Duration.ofMinutes(100), Set.of("Comedy"), "Highest rating"),
                new MovieRating(3, "A-Movie", 8.0f, 15, Duration.ofMinutes(80), Set.of("Action"), "Second highest"),
                new MovieRating(1, "B-Movie", 7.0f, 10, Duration.ofMinutes(90), Set.of("Drama"), "B first alphabetically")
            ),
            sortedByName,
            "Should sort alphabetically by name"
        );

        Stream<MovieRating> preSorted = service.getRatingsSortedByField(MovieRating::name, true);
        List<MovieRating> sortedByNameThenRatingDesc = service
            .getRatingsSortedByField(preSorted, MovieRating::value, false)
            .toList();
        assertEquals(
            List.of(
                new MovieRating(2, "A-Movie", 9.0f, 5, Duration.ofMinutes(100), Set.of("Comedy"), "Highest rating"),
                new MovieRating(3, "A-Movie", 8.0f, 15, Duration.ofMinutes(80), Set.of("Action"), "Second highest"),
                new MovieRating(1, "B-Movie", 7.0f, 10, Duration.ofMinutes(90), Set.of("Drama"), "B first alphabetically")
            ),
            sortedByNameThenRatingDesc,
            "Secondary sort should keep higher ratings first within the same name"
        );
    }

    @Test
    void testGetRatingsGreaterThan() {
        MovieRatingsService service = serviceWith(
            new MovieRating(1, "Movie 1", 6.5f, 120, Duration.ofMinutes(90), Set.of("Action"), "Description 1"),
            new MovieRating(2, "Movie 2", 7.5f, 100, Duration.ofMinutes(120), Set.of("Drama"), "Description 2")
        );

        assertEquals(1, service.getRatingsGreaterThan(7.0).count(), "Should return movies with ratings greater than 7.0");
    }

    @Test
    void testGetNamesSortedAlphabetically() {
        MovieRatingsService service = serviceWith(
            new MovieRating(2, "Zebra", 8.5f, 100, Duration.ofMinutes(90), Set.of("Action"), "Description of Zebra"),
            new MovieRating(1, "Apple", 7.5f, 120, Duration.ofMinutes(120), Set.of("Drama"), "Description of Apple")
        );

        List<String> names = service.getNamesSortedAlphabetically().toList();
        assertEquals(List.of("Apple", "Zebra"), names, "Names should be sorted alphabetically");
    }

    @Test
    void testGetDistinctGenres() {
        MovieRatingsService service = serviceWith(
            new MovieRating(1, "Movie 1", 8.5f, 100, Duration.ofMinutes(90), Set.of("Action", "Adventure"), "Description 1"),
            new MovieRating(2, "Movie 2", 7.5f, 120, Duration.ofMinutes(120), Set.of("Drama", "Action"), "Description 2")
        );

        Set<String> genres = service.getDistinctGenres().collect(Collectors.toSet());
        assertEquals(Set.of("Action", "Adventure", "Drama"), genres, "Should return distinct genres");
    }

    @Test
    void testGetMoviesHavingAllGenres() {
        MovieRatingsService service = serviceWith(
            new MovieRating(1, "Movie 1", 8.5f, 100, Duration.ofMinutes(90), Set.of("Action", "Adventure"), "Description 1"),
            new MovieRating(2, "Movie 2", 7.5f, 120, Duration.ofMinutes(120), Set.of("Drama"), "Description 2")
        );

        assertEquals(1, service.getMoviesHavingAllGenres(Set.of("Action", "Adventure")).count(), "Should return movies having all specified genres");
    }

    @Test
    void testGetTotalVoteCountOverAllRatings() {
        MovieRatingsService service = serviceWith(
            new MovieRating(1, "Movie 1", 8.5f, 100, Duration.ofMinutes(90), Set.of("Action"), "Description 1"),
            new MovieRating(2, "Movie 2", 7.5f, 120, Duration.ofMinutes(120), Set.of("Drama"), "Description 2")
        );

        assertEquals(220, service.getTotalVoteCountOverAllRatings(), "Should return the total vote count over all movie ratings");
    }

    @Test
    void testGetMovieRatingsSortedByDuration() {
        MovieRatingsService service = serviceWith(
            new MovieRating(1, "Movie 1", 8.5f, 100, Duration.ofHours(2), Set.of("Action"), "Description 1"),
            new MovieRating(2, "Movie 2", 7.5f, 120, Duration.ofHours(1), Set.of("Drama"), "Description 2")
        );

        List<MovieRating> sortedAscending = service.getMovieRatingsSortedByDuration(true).toList();
        assertTrue(sortedAscending.get(0).runtime().compareTo(sortedAscending.get(1).runtime()) <= 0, "Should be sorted in ascending order by duration");

        List<MovieRating> sortedDescending = service.getMovieRatingsSortedByDuration(false).toList();
        assertTrue(sortedDescending.get(0).runtime().compareTo(sortedDescending.get(1).runtime()) >= 0, "Should be sorted in descending order by duration");
    }

    @Test
    void testGetAverageRatingOfMoviesHavingGenre() {
        MovieRatingsService service = serviceWith(
            new MovieRating(1, "Movie 1", 8.5f, 100, Duration.ofMinutes(90), Set.of("Action"), "Description 1"),
            new MovieRating(2, "Movie 2", 7.5f, 120, Duration.ofMinutes(120), Set.of("Action"), "Description 2")
        );

        assertEquals(8.0f, service.getAverageRatingOfMoviesHavingGenre("Action"), "Should return the average value of movies having the specified genres");
    }

    @Test
    void testFindMovieWithName() {
        MovieRatingsService service = serviceWith(
            new MovieRating(1, "Unique Name", 8.5f, 100, Duration.ofMinutes(90), Set.of("Action"), "Description 1")
        );

        assertTrue(service.findMovieWithName("Unique Name").isPresent(), "Should find a movie with the specified name");
        assertFalse(service.findMovieWithName("Nonexistent Name").isPresent(), "Should not find a movie with a nonexistent name");
    }

    @Test
    void testGetMoviesWithDurationLessThan() {
        MovieRatingsService service = serviceWith(
            new MovieRating(1, "Movie 1", 8.5f, 100, Duration.ofHours(2), Set.of("Action"), "Description 1"),
            new MovieRating(2, "Movie 2", 7.5f, 120, Duration.ofHours(1), Set.of("Drama"), "Description 2")
        );

        assertEquals(1, service.getMoviesWithDurationLessThan(Duration.ofMinutes(90)).count(), "Should return movies with duration less than specified");
    }

    @Test
    void testCsvBackedDatasetEndToEnd() {
        MovieRatingsService csvService = new MovieRatingsService(CsvBackedMovieRatingsDataset.getInstance());

        assertTrue(csvService.getRatings(1).findFirst().isPresent(), "Dataset should expose at least one movie");
        assertTrue(csvService.findMovieWithName("Oppenheimer").isPresent(), "CSV dataset should contain Oppenheimer");

        Float dramaAverage = csvService.getAverageRatingOfMoviesHavingGenre("Drama");
        assertFalse(Float.isNaN(dramaAverage), "Average rating for Drama should be calculable");
        assertTrue(dramaAverage > 0 && dramaAverage <= 10, "Average rating should fall within 0..10");

        long totalVotes = csvService.getTotalVoteCountOverAllRatings();
        assertTrue(totalVotes > 0, "Total votes should be greater than zero");
    }
}
