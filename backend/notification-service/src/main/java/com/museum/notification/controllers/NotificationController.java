package com.museum.notification.controllers;

import com.museum.notification.domain.aggregate.NotificationChannel;
import com.museum.notification.domain.dtos.NotificationRequestDto;
import com.museum.notification.services.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class NotificationController {

    private final NotificationService notificationService;

    @PutMapping("/telegram/{target}")
    public ResponseEntity<Void> sendTelegramNotification(
            @PathVariable String target,
            @Valid @RequestBody NotificationRequestDto request
    ) {
        notificationService.sendNotification(NotificationChannel.TELEGRAM, target, request.message());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/email/{email:.+}")
    public ResponseEntity<Void> sendEmailNotification(
            @PathVariable String email,
            @Valid @RequestBody NotificationRequestDto request
    ) {
        notificationService.sendNotification(NotificationChannel.EMAIL, email, request.message());
        return ResponseEntity.ok().build();
    }
}
