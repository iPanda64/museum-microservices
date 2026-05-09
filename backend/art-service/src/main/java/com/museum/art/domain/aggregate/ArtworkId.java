package com.museum.art.domain.aggregate;

public record ArtworkId(Integer value) {
    public ArtworkId {
        if (value == null)
            throw new IllegalArgumentException("Artwork ID must not be null.");
    }
}
