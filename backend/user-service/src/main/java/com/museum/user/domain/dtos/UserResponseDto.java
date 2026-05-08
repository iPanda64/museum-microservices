package com.museum.user.domain.dtos;

public record UserResponseDto(
    Integer userId,
    String email,
    String phoneNumber
){}
