package com.museum.artist.domain.daocontracts;

import com.museum.artist.domain.aggregate.Artist;
import com.museum.artist.domain.aggregate.ArtistId;

import java.util.List;
import java.util.Optional;

public interface ArtistDAO {

    List<Artist> findAll();

    Optional<Artist> findById(ArtistId artistId);

    List<Artist> findByName(String name);

    Artist save(Artist artist);

    void deleteById(ArtistId artistId);

    boolean existsById(ArtistId artistId);
}
