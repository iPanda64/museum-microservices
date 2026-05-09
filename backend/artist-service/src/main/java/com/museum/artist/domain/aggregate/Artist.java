package com.museum.artist.domain.aggregate;

import java.time.LocalDate;

public record Artist(
        ArtistId artistId,
        String fullName,
        LocalDate birthDate,
        String birthPlace,
        String nationality,
        String biography,
        String profilePhotoPath
) {
    public Artist {
        if (fullName == null || fullName.isBlank())
            throw new IllegalArgumentException("fullName must not be null or blank");
    }
}
