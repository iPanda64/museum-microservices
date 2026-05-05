package com.museum.auth.controllers;

import com.museum.auth.controllers.mappers.CredentialsControllerMapper;
import com.museum.auth.domain.dtos.CredentialsRequestDto;
import com.museum.auth.domain.dtos.CredentialsResponseDto;
import com.museum.auth.domain.dtos.LoginRequestDto;
import com.museum.auth.domain.aggregate.Credentials;
import com.museum.auth.domain.aggregate.NullCredentials;
import com.museum.auth.services.CredentialsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/credentials")
@RequiredArgsConstructor
public class CredentialsController {

    private final CredentialsService credentialsService;
    private final CredentialsControllerMapper credentialsControllerMapper;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<CredentialsResponseDto> getAllCredentials() {
        return credentialsService.getAllCredentials().stream()
                .map(credentialsControllerMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CredentialsResponseDto> getCredentialsById(@PathVariable Integer userId) {
        return credentialsService.getCredentialsById(userId)
                .map(credentialsControllerMapper::toResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CredentialsResponseDto> createCredentials(@RequestBody CredentialsRequestDto dto) {
        Credentials domain = credentialsControllerMapper.toDomain(dto);
        Credentials created = credentialsService.createCredentials(domain);
        return new ResponseEntity<>(credentialsControllerMapper.toResponseDto(created), HttpStatus.CREATED);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CredentialsResponseDto> updateCredentials(
            @PathVariable Integer userId,
            @RequestBody CredentialsRequestDto dto) {
        NullCredentials nullDomain = credentialsControllerMapper.toNullDomain(dto);
        Credentials updated = credentialsService.updateCredentials(userId, nullDomain);
        return ResponseEntity.ok(credentialsControllerMapper.toResponseDto(updated));
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCredentials(@PathVariable Integer userId) {
        credentialsService.deleteCredentials(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequest) {
        String token = credentialsService.login(loginRequest.username(), loginRequest.password());
        return ResponseEntity.ok(token);
    }
}
