package com.museum.art.domain.dtos;

import java.util.List;

public record ArtworkResponseDto(
    Integer artworkId,
    Integer artistId,
    String title,
    String artworkType,
    List<com.museum.art.domain.aggregate.ArtworkImage> images
){}
