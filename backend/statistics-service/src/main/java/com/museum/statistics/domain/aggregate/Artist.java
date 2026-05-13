package com.museum.statistics.domain.aggregate;

public record Artist(
        ArtistId artistId,
        String fullName
) {
    public Artist {
        if (fullName == null || fullName.isBlank())
            throw new IllegalArgumentException("fullName must not be null or blank");
    }
}
