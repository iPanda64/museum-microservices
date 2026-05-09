package com.museum.art.controllers;

import com.museum.art.controllers.mappers.ArtworkControllerMapper;
import com.museum.art.domain.dtos.ArtworkRequestDto;
import com.museum.art.domain.dtos.ArtworkResponseDto;
import com.museum.art.domain.aggregate.Artwork;
import com.museum.art.domain.aggregate.NullArtwork;
import com.museum.art.services.ArtworkService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/art")
public class ArtworkController {

    private final ArtworkService artworkService;
    private final ArtworkControllerMapper artworkControllerMapper;

    public ArtworkController(ArtworkService artworkService, ArtworkControllerMapper artworkControllerMapper) {
        this.artworkService = artworkService;
        this.artworkControllerMapper = artworkControllerMapper;
    }

    @GetMapping
    public List<ArtworkResponseDto> getAllArtworks(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order) {
        return artworkService.getAllArtworks(sortBy, order).stream()
                .map(artworkControllerMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{artworkId}")
    public ResponseEntity<ArtworkResponseDto> getArtworkById(@PathVariable Integer artworkId) {
        return artworkService.getArtworkById(artworkId)
                .map(artworkControllerMapper::toResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public List<ArtworkResponseDto> searchArtworksByTitle(@RequestParam String title) {
        return artworkService.searchArtworksByTitle(title).stream()
                .map(artworkControllerMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') || hasRole('MANAGER') || hasRole('EMPLOYEE')")
    public ResponseEntity<ArtworkResponseDto> createArtwork(@RequestBody ArtworkRequestDto dto) {
        Artwork domain = artworkControllerMapper.toDomain(dto);
        Artwork created = artworkService.createArtwork(domain);
        return new ResponseEntity<>(artworkControllerMapper.toResponseDto(created), HttpStatus.CREATED);
    }

    @PutMapping("/{artworkId}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('MANAGER') || hasRole('EMPLOYEE')")
    public ResponseEntity<ArtworkResponseDto> updateArtwork(
            @PathVariable Integer artworkId,
            @RequestBody ArtworkRequestDto dto) {
        NullArtwork nullDomain = artworkControllerMapper.toNullDomain(dto);
        Artwork updated = artworkService.updateArtwork(artworkId, nullDomain);
        return ResponseEntity.ok(artworkControllerMapper.toResponseDto(updated));
    }

    @DeleteMapping("/{artworkId}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('MANAGER') || hasRole('EMPLOYEE')")
    public ResponseEntity<Void> deleteArtwork(@PathVariable Integer artworkId) {
        artworkService.deleteArtwork(artworkId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{artworkId}/photo")
    @PreAuthorize("hasRole('ADMIN') || hasRole('MANAGER') || hasRole('EMPLOYEE')")
    public ResponseEntity<String> uploadPhoto(
            @PathVariable Integer artworkId,
            HttpServletRequest request) throws IOException {
        String contentType = request.getContentType();
        String fileName = request.getHeader("X-File-Name");
        if (fileName == null || fileName.isBlank()) {
            fileName = "art.jpg";
        }

        String path = artworkService.addArtworkImage(artworkId, fileName, request.getInputStream(), contentType);
        return ResponseEntity.ok(path);
    }

    @DeleteMapping("/{artworkId}/photo/{imageId}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('MANAGER') || hasRole('EMPLOYEE')")
    public ResponseEntity<Void> deletePhoto(@PathVariable Integer artworkId, @PathVariable Integer imageId) {
        artworkService.deleteArtworkImage(artworkId, imageId);
        return ResponseEntity.noContent().build();
    }
}
