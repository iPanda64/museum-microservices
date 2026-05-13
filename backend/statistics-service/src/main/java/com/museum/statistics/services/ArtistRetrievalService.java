package com.museum.statistics.services;

import com.museum.statistics.domain.aggregate.Artist;
import com.museum.statistics.domain.dtos.ArtistDto;
import com.museum.statistics.services.mappers.ArtistRetrievalMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArtistRetrievalService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ArtistRetrievalMapper artistRetrievalMapper;
    private static final String ARTIST_SERVICE_URL = "http://artist-service:8080/api/artists";

    public List<Artist> fetchAllArtists() {
        ArtistDto[] dtos = restTemplate.getForObject(ARTIST_SERVICE_URL, ArtistDto[].class);
        if (dtos == null) return List.of();

        return Arrays.stream(dtos)
                .map(artistRetrievalMapper::toDomain)
                .collect(Collectors.toList());
    }
}
