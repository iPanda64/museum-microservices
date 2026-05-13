package com.museum.statistics.domain.aggregate;

public record ArtworkImage(Integer imageId, String imagePath) {
    public ArtworkImage {
        if (imagePath == null || imagePath.isBlank())
            throw new IllegalArgumentException("Image path must not be null or blank");
    }
}
