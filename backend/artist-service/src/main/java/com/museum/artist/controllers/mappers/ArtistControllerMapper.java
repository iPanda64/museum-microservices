package com.museum.artist.controllers.mappers;

import com.museum.artist.domain.dtos.ArtistRequestDto;
import com.museum.artist.domain.dtos.ArtistResponseDto;
import com.museum.artist.domain.aggregate.*;
import org.springframework.stereotype.Component;

@Component
public class ArtistControllerMapper {

    public Artist toDomain(ArtistRequestDto dto) {
        if (dto == null) return null;

        return new Artist(
                null,
                dto.fullName(),
                dto.birthDate(),
                dto.birthPlace(),
                dto.nationality(),
                dto.biography(),
                dto.profilePhotoPath()
        );
    }

    public NullArtist toNullDomain(ArtistRequestDto dto) {
        if (dto == null) {
            return null;
        }

        return new NullArtist(
                dto.fullName(),
                dto.birthDate(),
                dto.birthPlace(),
                dto.nationality(),
                dto.biography(),
                dto.profilePhotoPath()
        );
    }

    public ArtistResponseDto toResponseDto(Artist domain) {
        if (domain == null) return null;

        return new ArtistResponseDto(
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
