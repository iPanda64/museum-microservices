package com.museum.artist.infrastructure.mappers;

import com.museum.artist.domain.aggregate.Artist;
import com.museum.artist.domain.aggregate.ArtistId;
import com.museum.artist.infrastructure.entities.ArtistEntity;
import org.springframework.stereotype.Component;

@Component
public class ArtistMapper {

    public Artist toDomain(ArtistEntity entity) {
        if (entity == null) return null;

        return new Artist(
                new ArtistId(entity.getArtistId()),
                entity.getFullName(),
                entity.getBirthDate(),
                entity.getBirthPlace(),
                entity.getNationality(),
                entity.getBiography(),
                entity.getProfilePhotoPath()
        );
    }
    public ArtistEntity toEntity(Artist domain) {
        if (domain == null) return null;

        return new ArtistEntity(
                domain.artistId() != null ? domain.artistId().value() : null,
                domain.fullName(),
                domain.birthDate(),
                domain.birthPlace(),
                domain.nationality(),
                domain.biography(),
                domain.profilePhotoPath()
        );
    }
}
