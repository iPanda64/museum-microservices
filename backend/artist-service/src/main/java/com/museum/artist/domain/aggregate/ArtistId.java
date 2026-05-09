package com.museum.artist.domain.aggregate;

public record ArtistId(Integer value){

    public ArtistId {
        if (value == null)
            throw new IllegalArgumentException("Artist ID must not be null.");
    }
}
