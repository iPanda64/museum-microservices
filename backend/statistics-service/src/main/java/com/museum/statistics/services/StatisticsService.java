package com.museum.statistics.services;

import com.museum.statistics.domain.aggregate.Artist;
import com.museum.statistics.domain.aggregate.Artwork;
import com.museum.statistics.domain.contracts.StatisticProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final ArtRetrievalService artRetrievalService;
    private final ArtistRetrievalService artistRetrievalService;
    private final StatisticProviderFactory providerFactory;

    public List<String> getAvailableStatistics() {
        return providerFactory.getRegisteredPaths();
    }

    public Map<String, Long> getStatistics(String type) {
        List<Artwork> artworks = artRetrievalService.fetchAllArtworks();
        List<Artist> artists = artistRetrievalService.fetchAllArtists();

        StatisticProvider provider = providerFactory.getProvider(type);
        return provider.calculate(artworks, artists);
    }
}
