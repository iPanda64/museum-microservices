package com.museum.auth.domain.dtos;

public record CredentialsRequestDto(
    Integer userId,
    String username,
    String password,
    String roleName
){}
