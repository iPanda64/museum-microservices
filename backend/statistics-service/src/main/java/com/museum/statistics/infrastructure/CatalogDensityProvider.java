package com.museum.statistics.infrastructure;

import com.museum.statistics.domain.aggregate.Artist;
import com.museum.statistics.domain.aggregate.Artwork;
import com.museum.statistics.domain.contracts.StatisticProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CatalogDensityProvider implements StatisticProvider {

    @Override
    public String getPath() {
        return "density";
    }

    @Override
    public Map<String, Long> calculate(List<Artwork> artworks, List<Artist> artists) {
        return artworks.stream()
                .collect(Collectors.groupingBy(
                        a -> String.valueOf(a.images().size()),
                        Collectors.counting()
                ));
    }
}
