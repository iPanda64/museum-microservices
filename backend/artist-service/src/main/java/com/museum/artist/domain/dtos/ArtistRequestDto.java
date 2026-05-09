package com.museum.artist.domain.dtos;

import java.time.LocalDate;

public record ArtistRequestDto(
    String fullName,
    LocalDate birthDate,
    String birthPlace,
    String nationality,
    String biography,
    String profilePhotoPath
){}
