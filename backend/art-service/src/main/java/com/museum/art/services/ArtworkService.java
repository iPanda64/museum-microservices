package com.museum.art.services;

import com.museum.art.domain.daocontracts.ArtworkDAO;
import com.museum.art.domain.daocontracts.ImageStorage;
import com.museum.art.domain.aggregate.Artwork;
import com.museum.art.domain.aggregate.ArtworkId;
import com.museum.art.domain.aggregate.ArtworkImage;
import com.museum.art.domain.aggregate.NullArtwork;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ArtworkService {

    private final ArtworkDAO artworkDAO;
    private final ImageStorage imageStorage;

    public ArtworkService(ArtworkDAO artworkDAO, ImageStorage imageStorage) {
        this.artworkDAO = artworkDAO;
        this.imageStorage = imageStorage;
    }

    public List<Artwork> getAllArtworks(String sortBy, String order) {
        return artworkDAO.findAll(sortBy, order);
    }

    public Optional<Artwork> getArtworkById(Integer id) {
        return artworkDAO.findById(new ArtworkId(id));
    }

    public List<Artwork> searchArtworksByTitle(String title) {
        return artworkDAO.findByTitle(title);
    }

    public Artwork createArtwork(Artwork artwork) {
        if (artwork.artworkId() != null && artworkDAO.existsById(artwork.artworkId())) {
            throw new IllegalArgumentException("Artwork with this ID already exists!");
        }
        return artworkDAO.save(artwork);
    }

    public void deleteArtwork(Integer id) {
        Artwork artwork = artworkDAO.findById(new ArtworkId(id)).orElse(null);
        if (artwork != null && artwork.images() != null) {
            for (ArtworkImage image : artwork.images()) {
                imageStorage.deleteFile(image.imagePath());
            }
        }
        artworkDAO.deleteById(new ArtworkId(id));
    }

    public String addArtworkImage(Integer artworkId, String originalFilename, InputStream inputStream, String contentType) {
        Artwork artwork = artworkDAO.findById(new ArtworkId(artworkId))
                .orElseThrow(() -> new IllegalArgumentException("Artwork not found"));

        if (artwork.images().size() >= 3) {
            throw new IllegalArgumentException("An artwork can have at most 3 images");
        }

        String fileName = "art-" + artworkId + "-" + System.currentTimeMillis() + "-" + originalFilename;
        String storedPath = imageStorage.uploadFile(fileName, inputStream, contentType);

        List<ArtworkImage> updatedImages = new ArrayList<>(artwork.images());
        updatedImages.add(new ArtworkImage(null, storedPath));

        Artwork updatedArtwork = new Artwork(
                artwork.artworkId(),
                artwork.artistId(),
                artwork.title(),
                artwork.artworkType(),
                updatedImages
        );

        artworkDAO.save(updatedArtwork);
        return storedPath;
    }

    public void deleteArtworkImage(Integer artworkId, Integer imageId) {
        Artwork artwork = artworkDAO.findById(new ArtworkId(artworkId))
                .orElseThrow(() -> new IllegalArgumentException("Artwork not found"));

        ArtworkImage imageToDelete = artwork.images().stream()
                .filter(img -> img.imageId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Image not found in this artwork"));

        imageStorage.deleteFile(imageToDelete.imagePath());

        List<ArtworkImage> updatedImages = artwork.images().stream()
                .filter(img -> !img.imageId().equals(imageId))
                .collect(java.util.stream.Collectors.toList());

        Artwork updatedArtwork = new Artwork(
                artwork.artworkId(),
                artwork.artistId(),
                artwork.title(),
                artwork.artworkType(),
                updatedImages
        );

        artworkDAO.save(updatedArtwork);
    }

    public Artwork updateArtwork(Integer artworkId, NullArtwork nullArtwork) {
        Artwork existing = artworkDAO.findById(new ArtworkId(artworkId))
                .orElseThrow(() -> new IllegalArgumentException("Artwork with ID " + artworkId + " not found."));

        Artwork artworkToSave = new Artwork(
                existing.artworkId(),
                nullArtwork.artistId() != null ? nullArtwork.artistId() : existing.artistId(),
                nullArtwork.title() != null && !nullArtwork.title().isBlank() ? nullArtwork.title() : existing.title(),
                nullArtwork.artworkType() != null && !nullArtwork.artworkType().isBlank() ? nullArtwork.artworkType() : existing.artworkType(),
                existing.images()
        );

        return artworkDAO.save(artworkToSave);
    }
}
