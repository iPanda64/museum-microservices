package com.museum.auth.domain.dtos;

public record CredentialsResponseDto(
    Integer userId,
    String username,
    String roleName
){}