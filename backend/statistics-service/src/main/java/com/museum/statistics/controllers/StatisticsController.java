package com.museum.statistics.controllers;

import com.museum.statistics.services.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<String>> getAvailableStatistics() {
        return ResponseEntity.ok(statisticsService.getAvailableStatistics());
    }

    @GetMapping("/{type}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Map<String, Long>> getStatistics(@PathVariable String type) {
        return ResponseEntity.ok(statisticsService.getStatistics(type));
    }
}
