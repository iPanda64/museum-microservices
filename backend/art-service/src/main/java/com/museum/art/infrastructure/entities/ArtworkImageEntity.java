package com.museum.art.infrastructure.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "artwork_image")
public class ArtworkImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Integer imageId;

    @Column(name = "image_path", nullable = false)
    private String imagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artwork_id", nullable = false)
    private ArtworkEntity artwork;

    public ArtworkImageEntity() {
    }

    public ArtworkImageEntity(Integer imageId, String imagePath, ArtworkEntity artwork) {
        this.imageId = imageId;
        this.imagePath = imagePath;
        this.artwork = artwork;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public ArtworkEntity getArtwork() {
        return artwork;
    }

    public void setArtwork(ArtworkEntity artwork) {
        this.artwork = artwork;
    }
}
