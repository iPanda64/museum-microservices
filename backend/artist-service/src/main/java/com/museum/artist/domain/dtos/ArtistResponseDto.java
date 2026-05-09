package com.museum.artist.domain.dtos;

import java.time.LocalDate;

public record ArtistResponseDto(
    Integer artistId,
    String fullName,
    LocalDate birthDate,
    String birthPlace,
    String nationality,
    String biography,
    String profilePhotoPath
){}
