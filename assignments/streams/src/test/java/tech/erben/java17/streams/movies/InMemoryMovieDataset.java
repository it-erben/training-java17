package tech.erben.java17.streams.movies;

import java.util.List;
import java.util.stream.Stream;

public class InMemoryMovieDataset implements MovieRatingsDataset{

    private final List<MovieRating> ratings;

    public InMemoryMovieDataset(List<MovieRating> ratings) {
        this.ratings = ratings;
    }

    @Override
    public Stream<MovieRating> load() {
        return ratings.stream();
    }
}
