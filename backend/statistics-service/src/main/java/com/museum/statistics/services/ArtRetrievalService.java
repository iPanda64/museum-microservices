package com.museum.statistics.services;

import com.museum.statistics.domain.aggregate.Artwork;
import com.museum.statistics.domain.dtos.ArtworkDto;
import com.museum.statistics.services.mappers.ArtRetrievalMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArtRetrievalService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ArtRetrievalMapper artRetrievalMapper;
    private static final String ART_SERVICE_URL = "http://art-service:8080/api/art";

    public List<Artwork> fetchAllArtworks() {
        ArtworkDto[] dtos = restTemplate.getForObject(ART_SERVICE_URL, ArtworkDto[].class);
        if (dtos == null) return List.of();

        return Arrays.stream(dtos)
                .map(artRetrievalMapper::toDomain)
                .collect(Collectors.toList());
    }
}
