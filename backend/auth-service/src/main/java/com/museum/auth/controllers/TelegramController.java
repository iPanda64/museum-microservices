package com.museum.auth.controllers;

import com.museum.auth.services.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/credentials/telegram")
@RequiredArgsConstructor
public class TelegramController {

    private final TelegramService telegramService;

    @GetMapping("/link/{id}")
    public ResponseEntity<Map<String, String>> getTelegramLink(@PathVariable Integer id) {
        String link = telegramService.getTelegramLink(id);
        return ResponseEntity.ok(Collections.singletonMap("url", link));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> isTelegramLinked(@PathVariable Integer id) {
        boolean linked = telegramService.isTelegramLinked(id);
        return ResponseEntity.ok(Collections.singletonMap("linked", linked));
    }

    @GetMapping("/chat-id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> getTelegramChatId(@PathVariable Integer id) {
        try {
            Long chatId = telegramService.getTelegramChatId(id);
            return ResponseEntity.ok(chatId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> unlinkTelegram(@PathVariable Integer id) {
        telegramService.unlinkTelegram(id);
        return ResponseEntity.noContent().build();
    }
}
