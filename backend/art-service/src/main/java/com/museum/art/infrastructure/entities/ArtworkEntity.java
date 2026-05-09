package com.museum.art.infrastructure.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "artwork")
public class ArtworkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artwork_id")
    private Integer artworkId;

    @Column(name = "artist_id", nullable = false)
    private Integer artistId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "artwork_type")
    private String artworkType;

    @OneToMany(mappedBy = "artwork", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ArtworkImageEntity> images;

    public ArtworkEntity() {
    }

    public ArtworkEntity(Integer artworkId, Integer artistId, String title, String artworkType) {
        this.artworkId = artworkId;
        this.artistId = artistId;
        this.title = title;
        this.artworkType = artworkType;
    }

    public Integer getArtworkId() {
        return artworkId;
    }

    public void setArtworkId(Integer artworkId) {
        this.artworkId = artworkId;
    }

    public Integer getArtistId() {
        return artistId;
    }

    public void setArtistId(Integer artistId) {
        this.artistId = artistId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtworkType() {
        return artworkType;
    }

    public void setArtworkType(String artworkType) {
        this.artworkType = artworkType;
    }

    public List<ArtworkImageEntity> getImages() {
        return images;
    }

    public void setImages(List<ArtworkImageEntity> images) {
        this.images = images;
    }
}
