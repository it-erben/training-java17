package tech.erben.java17.streams.movies;

import java.util.stream.Stream;

public interface MovieRatingsDataset {
    Stream<MovieRating> load();

}

