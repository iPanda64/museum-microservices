package com.museum.art.domain.aggregate;

/**
 * A data holder that allows nulls, used for processing partial updates for Artwork.
 */
public record NullArtwork(
        Integer artistId,
        String title,
        String artworkType
) {}
