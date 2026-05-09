package com.museum.art.domain.daocontracts;

import com.museum.art.domain.aggregate.Artwork;
import com.museum.art.domain.aggregate.ArtworkId;

import java.util.List;
import java.util.Optional;

public interface ArtworkDAO {

    List<Artwork> findAll(String sortBy, String order);

    Optional<Artwork> findById(ArtworkId artworkId);

    List<Artwork> findByTitle(String title);

    Artwork save(Artwork artwork);

    void deleteById(ArtworkId artworkId);

    boolean existsById(ArtworkId artworkId);
}
