package com.museum.statistics.domain.dtos;

import java.time.LocalDate;

public record ArtistDto(
    Integer artistId,
    String fullName,
    LocalDate birthDate,
    String birthPlace,
    String nationality,
    String biography,
    String profilePhotoPath
){}
