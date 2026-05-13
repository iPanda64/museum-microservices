package com.museum.statistics.infrastructure;

import com.museum.statistics.domain.aggregate.Artist;
import com.museum.statistics.domain.aggregate.Artwork;
import com.museum.statistics.domain.contracts.StatisticProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ArtistWorkCountProvider implements StatisticProvider {

    @Override
    public String getPath() {
        return "artists";
    }

    @Override
    public Map<String, Long> calculate(List<Artwork> artworks, List<Artist> artists) {
        Map<Integer, String> artistIdToName = artists.stream()
                .collect(Collectors.toMap(a -> a.artistId().value(), Artist::fullName));

        return artworks.stream()
                .collect(Collectors.groupingBy(
                        a -> artistIdToName.getOrDefault(a.artistId(), "Unknown Artist (ID: " + a.artistId() + ")"),
                        Collectors.counting()
                ));
    }
}
