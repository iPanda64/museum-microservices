package com.museum.statistics.domain.contracts;

import com.museum.statistics.domain.aggregate.Artist;
import com.museum.statistics.domain.aggregate.Artwork;

import java.util.List;
import java.util.Map;

public interface StatisticProvider {
    String getPath();
    Map<String, Long> calculate(List<Artwork> artworks, List<Artist> artists);
}
