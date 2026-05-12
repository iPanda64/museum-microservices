package com.museum.export.services.mappers;

import com.museum.export.domain.aggregate.Artwork;
import com.museum.export.domain.aggregate.ArtworkId;
import com.museum.export.domain.aggregate.ArtworkImage;
import com.museum.export.domain.dtos.ArtworkDto;
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
