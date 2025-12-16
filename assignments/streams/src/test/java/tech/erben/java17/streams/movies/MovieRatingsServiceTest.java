package tech.erben.java17.streams.movies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class MovieRatingsServiceTest {

    private MovieRatingsDataset mockDataset;
    private MovieRatingsService service;

    @BeforeEach
    void setUp() {
        mockDataset = Mockito.mock(MovieRatingsDataset.class);
        service = new MovieRatingsService(mockDataset);
    }

    @Test
    void testGetRatingsWithLimit() {
        List<MovieRating> expectedStream = List.of(
            new MovieRating(1, "Movie 1", 8.5f, 120, Duration.ofMinutes(90), Set.of("Action"), "Description 1"),
            new MovieRating(2, "Movie 2", 7.5f, 100, Duration.ofMinutes(120), Set.of("Drama"), "Description 2"));
        when(mockDataset.load()).thenAnswer(invocation -> expectedStream.stream());

        assertEquals(2, service.getRatings(2).count(), "Should return all movie ratings when limit is equal to dataset size");
        assertEquals(1, service.getRatings(1).count(), "Should return limited movie ratings when limit is less than dataset size");
    }

    @Test
    void testGetRatingsSortedByVotes() {
        List<MovieRating> unsortedStream = List.of(
            new MovieRating(1, "Movie 1", 8.5f, 100, Duration.ofMinutes(90), Set.of("Action"), "Description 1"),
            new MovieRating(2, "Movie 2", 7.5f, 120, Duration.ofMinutes(120), Set.of("Drama"), "Description 2"));
        when(mockDataset.load()).thenAnswer(invocation -> unsortedStream.stream());

        List<MovieRating> sortedAscending = service.getRatingsSortedByVotes(true).toList();
        assertTrue(sortedAscending.get(0).votes() <= sortedAscending.get(1).votes(), "Should be sorted in ascending order by votes");

        List<MovieRating> sortedDescending = service.getRatingsSortedByVotes(false).toList();
        assertTrue(sortedDescending.get(0).votes() >= sortedDescending.get(1).votes(), "Should be sorted in descending order by votes");
    }

    @Test
    void testGetRatingsGreaterThan() {
        Stream<MovieRating> ratingsStream = Stream.of(
            new MovieRating(1, "Movie 1", 6.5f, 120, Duration.ofMinutes(90), Set.of("Action"), "Description 1"),
            new MovieRating(2, "Movie 2", 7.5f, 100, Duration.ofMinutes(120), Set.of("Drama"), "Description 2"));
        when(mockDataset.load()).thenReturn(ratingsStream);

        assertEquals(1, service.getRatingsGreaterThan(7.0).count(), "Should return movies with ratings greater than 7.0");
    }

    @Test
    void testGetNamesSortedAlphabetically() {
        Stream<MovieRating> ratingsStream = Stream.of(
            new MovieRating(2, "Zebra", 8.5f, 100, Duration.ofMinutes(90), Set.of("Action"), "Description of Zebra"),
            new MovieRating(1, "Apple", 7.5f, 120, Duration.ofMinutes(120), Set.of("Drama"), "Description of Apple"));
        when(mockDataset.load()).thenReturn(ratingsStream);

        List<String> names = service.getNamesSortedAlphabetically().collect(Collectors.toList());
        assertEquals(Arrays.asList("Apple", "Zebra"), names, "Names should be sorted alphabetically");
    }

    @Test
    void testGetDistinctGenres() {
        Stream<MovieRating> ratingsStream = Stream.of(
            new MovieRating(1, "Movie 1", 8.5f, 100, Duration.ofMinutes(90), new HashSet<>(Arrays.asList("Action", "Adventure")), "Description 1"),
            new MovieRating(2, "Movie 2", 7.5f, 120, Duration.ofMinutes(120), new HashSet<>(Arrays.asList("Drama", "Action")), "Description 2"));
        when(mockDataset.load()).thenReturn(ratingsStream);

        Set<String> genres = service.getDistinctGenres().collect(Collectors.toSet());
        assertEquals(new HashSet<>(Arrays.asList("Action", "Adventure", "Drama")), genres, "Should return distinct genres");
    }

    @Test
    void testGetMoviesHavingAllGenres() {
        Stream<MovieRating> ratingsStream = Stream.of(
            new MovieRating(1, "Movie 1", 8.5f, 100, Duration.ofMinutes(90), Set.of("Action", "Adventure"), "Description 1"),
            new MovieRating(2, "Movie 2", 7.5f, 120, Duration.ofMinutes(120), Set.of("Drama"), "Description 2"));
        when(mockDataset.load()).thenReturn(ratingsStream);

        assertEquals(1, service.getMoviesHavingAllGenres(Set.of("Action", "Adventure")).count(), "Should return movies having all specified genres");
    }

    @Test
    void testGetTotalVoteCountOverAllRatings() {
        Stream<MovieRating> ratingsStream = Stream.of(
            new MovieRating(1, "Movie 1", 8.5f, 100, Duration.ofMinutes(90), Set.of("Action"), "Description 1"),
            new MovieRating(2, "Movie 2", 7.5f, 120, Duration.ofMinutes(120), Set.of("Drama"), "Description 2"));
        when(mockDataset.load()).thenReturn(ratingsStream);

        assertEquals(220, service.getTotalVoteCountOverAllRatings(), "Should return the total vote count over all movie ratings");
    }

    @Test
    void testGetMovieRatingsSortedByDuration() {
        List<MovieRating> unsortedStream = List.of(
            new MovieRating(1, "Movie 1", 8.5f, 100, Duration.ofHours(2), Set.of("Action"), "Description 1"),
            new MovieRating(2, "Movie 2", 7.5f, 120, Duration.ofHours(1), Set.of("Drama"), "Description 2"));
        when(mockDataset.load()).thenAnswer(invocation -> unsortedStream.stream());

        List<MovieRating> sortedAscending = service.getMovieRatingsSortedByDuration(true).toList();
        assertTrue(sortedAscending.get(0).runtime().compareTo(sortedAscending.get(1).runtime()) <= 0, "Should be sorted in ascending order by duration");

        List<MovieRating> sortedDescending = service.getMovieRatingsSortedByDuration(false).toList();
        assertTrue(sortedDescending.get(0).runtime().compareTo(sortedDescending.get(1).runtime()) >= 0, "Should be sorted in descending order by duration");
    }

    @Test
    void testGetAverageRatingOfMoviesHavingGenre() {
        Stream<MovieRating> ratingsStream = Stream.of(
            new MovieRating(1, "Movie 1", 8.5f, 100, Duration.ofMinutes(90), Set.of("Action"), "Description 1"),
            new MovieRating(2, "Movie 2", 7.5f, 120, Duration.ofMinutes(120), Set.of("Action"), "Description 2"));
        when(mockDataset.load()).thenReturn(ratingsStream);

        assertEquals(8.0f, service.getAverageRatingOfMoviesHavingGenre("Action"), "Should return the average value of movies having the specified genres");
    }

    @Test
    void testFindMovieWithName() {
        List<MovieRating> ratingsStream = List.of(
            new MovieRating(1, "Unique Name", 8.5f, 100, Duration.ofMinutes(90), Set.of("Action"), "Description 1"));
        when(mockDataset.load()).thenAnswer(invocation -> ratingsStream.stream());

        assertTrue(service.findMovieWithName("Unique Name").isPresent(), "Should find a movie with the specified name");
        assertFalse(service.findMovieWithName("Nonexistent Name").isPresent(), "Should not find a movie with a nonexistent name");
    }

    @Test
    void testGetMoviesWithDurationLessThan() {
        List<MovieRating> ratingsStream = List.of(
            new MovieRating(1, "Movie 1", 8.5f, 100, Duration.ofHours(2), Set.of("Action"), "Description 1"),
            new MovieRating(2, "Movie 2", 7.5f, 120, Duration.ofHours(1), Set.of("Drama"), "Description 2"));
        when(mockDataset.load()).thenAnswer(invocation -> ratingsStream.stream());

        assertEquals(1, service.getMoviesWithDurationLessThan(Duration.ofMinutes(90)).count(), "Should return movies with duration less than specified");
    }
}
