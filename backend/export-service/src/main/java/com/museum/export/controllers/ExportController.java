package com.museum.export.controllers;

import com.museum.export.domain.aggregate.Artwork;
import com.museum.export.domain.dtos.ExportRequestDto;
import com.museum.export.services.ArtRetrievalService;
import com.museum.export.services.ExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
public class ExportController {

    private final ExportService exportService;
    private final ArtRetrievalService artRetrievalService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') || hasRole('MANAGER') || hasRole('EMPLOYEE')")
    public ResponseEntity<byte[]> exportArtworks(@RequestBody final ExportRequestDto requestDto) {
        List<Artwork> artworks = artRetrievalService.fetchAllArtworks();
        String format = requestDto.format();
        String result = exportService.generateExport(format, artworks);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"artworks." + format.toLowerCase() + "\"")
                .body(result.getBytes(StandardCharsets.UTF_8));
    }

    @GetMapping
    public ResponseEntity<List<String>> getAvaliableFormats() {
        return ResponseEntity.ok(exportService.getAvailableFormats());
    }
}
