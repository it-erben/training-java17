package tech.erben.java17.streams.movies;

import java.time.Duration;
import java.util.Set;

public record MovieRating(
    int id,
    String name,
    Float rating,
    Integer votes,
    Duration runtime,
    Set<String> genre,
    String description
) {}
