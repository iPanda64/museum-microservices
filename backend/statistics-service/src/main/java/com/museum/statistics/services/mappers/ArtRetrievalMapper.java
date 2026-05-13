package com.museum.statistics.services.mappers;

import com.museum.statistics.domain.aggregate.Artwork;
import com.museum.statistics.domain.aggregate.ArtworkId;
import com.museum.statistics.domain.aggregate.ArtworkImage;
import com.museum.statistics.domain.dtos.ArtworkDto;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ArtRetrievalMapper {

    public Artwork toDomain(ArtworkDto dto) {
        if (dto == null) return null;

        return new Artwork(
                new ArtworkId(dto.artworkId()),
                dto.artistId(),
                dto.title(),
                dto.artworkType(),
                dto.images().stream()
                        .map(img -> new ArtworkImage(img.imageId(), img.imagePath()))
                        .collect(Collectors.toList())
        );
    }
}
