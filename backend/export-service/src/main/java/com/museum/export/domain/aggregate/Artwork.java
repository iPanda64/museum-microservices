package com.museum.export.domain.aggregate;

import java.util.ArrayList;
import java.util.List;

public record Artwork(
        ArtworkId artworkId,
        Integer artistId,
        String title,
        String artworkType,
        List<ArtworkImage> images
) {
    public Artwork {
        if (artistId == null)
            throw new IllegalArgumentException("Artist ID must not be null");
        if (title == null || title.isBlank())
            throw new IllegalArgumentException("Title must not be null or blank");
        if (images == null) {
            images = new ArrayList<>();
        }
        if (images.size() > 3) {
            throw new IllegalArgumentException("An artwork can have at most 3 images");
        }
    }
}
