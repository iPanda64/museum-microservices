package com.museum.art.infrastructure.mappers;

import com.museum.art.domain.aggregate.Artwork;
import com.museum.art.domain.aggregate.ArtworkId;
import com.museum.art.domain.aggregate.ArtworkImage;
import com.museum.art.infrastructure.entities.ArtworkEntity;
import com.museum.art.infrastructure.entities.ArtworkImageEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ArtworkMapper {

    public Artwork toDomain(ArtworkEntity entity) {
        if (entity == null) return null;

        List<ArtworkImage> images = new ArrayList<>();
        if (entity.getImages() != null) {
            images = entity.getImages().stream()
                    .map(img -> new ArtworkImage(img.getImageId(), img.getImagePath()))
                    .collect(Collectors.toList());
        }

        return new Artwork(
                new ArtworkId(entity.getArtworkId()),
                entity.getArtistId(),
                entity.getTitle(),
                entity.getArtworkType(),
                images
        );
    }

    public ArtworkEntity toEntity(Artwork domain) {
        if (domain == null) return null;

        ArtworkEntity entity = new ArtworkEntity(
                domain.artworkId() != null ? domain.artworkId().value() : null,
                domain.artistId(),
                domain.title(),
                domain.artworkType()
        );

        if (domain.images() != null) {
            List<ArtworkImageEntity> imageEntities = domain.images().stream()
                    .map(img -> new ArtworkImageEntity(img.imageId(), img.imagePath(), entity))
                    .collect(Collectors.toList());
            entity.setImages(imageEntities);
        }

        return entity;
    }
}
