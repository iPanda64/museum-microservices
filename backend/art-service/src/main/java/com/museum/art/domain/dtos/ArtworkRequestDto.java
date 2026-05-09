package com.museum.art.domain.dtos;

public record ArtworkRequestDto(
    Integer artistId,
    String title,
    String artworkType
){}
