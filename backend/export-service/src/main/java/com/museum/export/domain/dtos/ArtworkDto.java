package com.museum.export.domain.dtos;

import java.util.List;

public record ArtworkDto(
    Integer artworkId,
    Integer artistId,
    String title,
    String artworkType,
    List<ArtworkImageDto> images
){}
