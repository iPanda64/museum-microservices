package com.museum.art.controllers.mappers;

import com.museum.art.domain.dtos.ArtworkRequestDto;
import com.museum.art.domain.dtos.ArtworkResponseDto;
import com.museum.art.domain.aggregate.*;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ArtworkControllerMapper {

    public Artwork toDomain(ArtworkRequestDto dto) {
        if (dto == null) return null;

        return new Artwork(
                null,
                dto.artistId(),
                dto.title(),
                dto.artworkType(),
                null
        );
    }

    public NullArtwork toNullDomain(ArtworkRequestDto dto) {
        if (dto == null) return null;

        return new NullArtwork(
                dto.artistId(),
                dto.title(),
                dto.artworkType()
        );
    }

    public ArtworkResponseDto toResponseDto(Artwork domain) {
        if (domain == null) return null;

        return new ArtworkResponseDto(
                domain.artworkId() != null ? domain.artworkId().value() : null,
                domain.artistId(),
                domain.title(),
                domain.artworkType(),
                domain.images()
        );
    }
}
