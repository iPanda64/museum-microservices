package com.museum.artist.controllers;

import com.museum.artist.controllers.mappers.ArtistControllerMapper;
import com.museum.artist.domain.dtos.ArtistRequestDto;
import com.museum.artist.domain.dtos.ArtistResponseDto;
import com.museum.artist.domain.aggregate.Artist;
import com.museum.artist.domain.aggregate.NullArtist;
import com.museum.artist.services.ArtistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    private final ArtistService artistService;
    private final ArtistControllerMapper artistControllerMapper;

    public ArtistController(ArtistService artistService, ArtistControllerMapper artistControllerMapper) {
        this.artistService = artistService;
        this.artistControllerMapper = artistControllerMapper;
    }

    @GetMapping
    public List<ArtistResponseDto> getAllArtists() {
        return artistService.getAllArtists().stream()
                .map(artistControllerMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{artistId}")
    public ResponseEntity<ArtistResponseDto> getArtistById(@PathVariable Integer artistId) {
        return artistService.getArtistById(artistId)
                .map(artistControllerMapper::toResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public List<ArtistResponseDto> searchArtistsByName(@RequestParam String name) {
        return artistService.searchArtistsByName(name).stream()
                .map(artistControllerMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') || hasRole('MANAGER') || hasRole('EMPLOYEE')")
    public ResponseEntity<ArtistResponseDto> createArtist(@RequestBody ArtistRequestDto dto) {
        Artist domain = artistControllerMapper.toDomain(dto);
        Artist created = artistService.createArtist(domain);
        return new ResponseEntity<>(artistControllerMapper.toResponseDto(created), HttpStatus.CREATED);
    }

    @PutMapping("/{artistId}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('MANAGER') || hasRole('EMPLOYEE')")
    public ResponseEntity<ArtistResponseDto> updateArtist(
            @PathVariable Integer artistId,
            @RequestBody ArtistRequestDto dto) {
        NullArtist nullDomain = artistControllerMapper.toNullDomain(dto);
        Artist updated = artistService.updateArtist(artistId, nullDomain);
        return ResponseEntity.ok(artistControllerMapper.toResponseDto(updated));
    }

    @DeleteMapping("/{artistId}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('MANAGER') || hasRole('EMPLOYEE')")
    public ResponseEntity<Void> deleteArtist(@PathVariable Integer artistId) {
        artistService.deleteArtist(artistId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{artistId}/photo")
    @PreAuthorize("hasRole('ADMIN') || hasRole('MANAGER') || hasRole('EMPLOYEE')")
    public ResponseEntity<String> uploadPhoto(
            @PathVariable Integer artistId,
            jakarta.servlet.http.HttpServletRequest request) throws java.io.IOException {
        String contentType = request.getContentType();
        String fileName = request.getHeader("X-File-Name");
        if (fileName == null || fileName.isBlank()) {
            fileName = "photo.jpg";
        }

        String path = artistService.uploadProfilePhoto(artistId, fileName, request.getInputStream(), contentType);
        return ResponseEntity.ok(path);
    }

    @DeleteMapping("/{artistId}/photo")
    @PreAuthorize("hasRole('ADMIN') || hasRole('MANAGER') || hasRole('EMPLOYEE')")
    public ResponseEntity<Void> deletePhoto(@PathVariable Integer artistId) {
        artistService.deleteProfilePhoto(artistId);
        return ResponseEntity.noContent().build();
    }
}
