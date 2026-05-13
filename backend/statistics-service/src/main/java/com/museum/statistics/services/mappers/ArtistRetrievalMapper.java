package com.museum.statistics.services.mappers;

import com.museum.statistics.domain.aggregate.Artist;
import com.museum.statistics.domain.aggregate.ArtistId;
import com.museum.statistics.domain.dtos.ArtistDto;
import org.springframework.stereotype.Component;

@Component
public class ArtistRetrievalMapper {

    public Artist toDomain(ArtistDto dto) {
        if (dto == null) return null;

        return new Artist(
                new ArtistId(dto.artistId()),
                dto.fullName()
        );
    }
}
