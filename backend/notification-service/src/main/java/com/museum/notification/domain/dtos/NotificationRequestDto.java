package com.museum.notification.domain.dtos;

import jakarta.validation.constraints.NotBlank;

public record NotificationRequestDto(
    @NotBlank(message = "Message must not be blank")
    String message
){}
