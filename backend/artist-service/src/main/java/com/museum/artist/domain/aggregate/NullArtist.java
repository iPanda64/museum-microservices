package com.museum.artist.domain.aggregate;

import java.time.LocalDate;

/**
 * A data holder that allows nulls, used for processing partial updates for Artist.
 */
public record NullArtist(
        String fullName,
        LocalDate birthDate,
        String birthPlace,
        String nationality,
        String biography,
        String profilePhotoPath
) {}
