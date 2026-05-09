package com.museum.artist.services;

import com.museum.artist.domain.daocontracts.ArtistDAO;
import com.museum.artist.domain.daocontracts.StorageDAO;
import com.museum.artist.domain.aggregate.Artist;
import com.museum.artist.domain.aggregate.ArtistId;
import com.museum.artist.domain.aggregate.NullArtist;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ArtistService {

    private final ArtistDAO artistDAO;
    private final StorageDAO storageDAO;

    public ArtistService(ArtistDAO artistDAO, StorageDAO storageDAO) {
        this.artistDAO = artistDAO;
        this.storageDAO = storageDAO;
    }

    public List<Artist> getAllArtists() {
        return artistDAO.findAll();
    }

    public Optional<Artist> getArtistById(Integer id) {
        return artistDAO.findById(new ArtistId(id));
    }

    public List<Artist> searchArtistsByName(String name) {
        return artistDAO.findByName(name);
    }

    public Artist createArtist(Artist artist) {
        if (artist.artistId() != null && artistDAO.existsById(artist.artistId())) {
            throw new IllegalArgumentException("Artist with this ID already exists!");
        }
        return artistDAO.save(artist);
    }

    public void deleteArtist(Integer id) {
        Artist artist = artistDAO.findById(new ArtistId(id)).orElse(null);
        if (artist != null && artist.profilePhotoPath() != null) {
            storageDAO.deleteFile(artist.profilePhotoPath());
        }
        artistDAO.deleteById(new ArtistId(id));
    }

    public String uploadProfilePhoto(Integer artistId, String originalFilename, InputStream inputStream, String contentType) {
        Artist artist = artistDAO.findById(new ArtistId(artistId))
                .orElseThrow(() -> new IllegalArgumentException("Artist not found"));

        if (artist.profilePhotoPath() != null) {
            try {
                storageDAO.deleteFile(artist.profilePhotoPath());
            } catch (Exception e) {
            }
        }

        String fileName = "artist-" + artistId + "-" + originalFilename;
        String storedPath = storageDAO.uploadFile(fileName, inputStream, contentType);

        Artist updatedArtist = new Artist(
                artist.artistId(),
                artist.fullName(),
                artist.birthDate(),
                artist.birthPlace(),
                artist.nationality(),
                artist.biography(),
                storedPath
        );

        artistDAO.save(updatedArtist);
        return storedPath;
    }

    public void deleteProfilePhoto(Integer artistId) {
        Artist artist = artistDAO.findById(new ArtistId(artistId))
                .orElseThrow(() -> new IllegalArgumentException("Artist not found"));

        if (artist.profilePhotoPath() != null) {
            storageDAO.deleteFile(artist.profilePhotoPath());
            
            Artist updatedArtist = new Artist(
                    artist.artistId(),
                    artist.fullName(),
                    artist.birthDate(),
                    artist.birthPlace(),
                    artist.nationality(),
                    artist.biography(),
                    null
            );
            artistDAO.save(updatedArtist);
        }
    }

    public Artist updateArtist(Integer artistId, NullArtist nullArtist) {
        Artist existing = artistDAO.findById(new ArtistId(artistId))
                .orElseThrow(() -> new IllegalArgumentException("Artist with ID " + artistId + " not found."));

        Artist artistToSave = new Artist(
                existing.artistId(),
                nullArtist.fullName() != null && !nullArtist.fullName().isBlank() ? nullArtist.fullName() : existing.fullName(),
                nullArtist.birthDate() != null ? nullArtist.birthDate() : existing.birthDate(),
                nullArtist.birthPlace() != null && !nullArtist.birthPlace().isBlank() ? nullArtist.birthPlace() : existing.birthPlace(),
                nullArtist.nationality() != null && !nullArtist.nationality().isBlank() ? nullArtist.nationality() : existing.nationality(),
                nullArtist.biography() != null && !nullArtist.biography().isBlank() ? nullArtist.biography() : existing.biography(),
                nullArtist.profilePhotoPath() != null && !nullArtist.profilePhotoPath().isBlank() ? nullArtist.profilePhotoPath() : existing.profilePhotoPath()
        );

        return artistDAO.save(artistToSave);
    }
}
