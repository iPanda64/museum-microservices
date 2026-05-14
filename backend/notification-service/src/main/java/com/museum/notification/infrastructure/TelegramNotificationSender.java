package com.museum.notification.infrastructure;

import com.museum.notification.domain.aggregate.NotificationChannel;
import com.museum.notification.domain.contracts.NotificationSender;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class TelegramNotificationSender implements NotificationSender {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${services.auth.url}")
    private String authServiceUrl;

    @Override
    public boolean supports(NotificationChannel channel) {
        return channel == NotificationChannel.TELEGRAM;
    }

    @Override
    public void send(String target, String message) {
        if (botToken == null || botToken.isBlank()) {
            throw new IllegalStateException("Telegram bot token is not configured.");
        }

        Long chatId;
        if (target.startsWith("chat:")) {
            chatId = Long.parseLong(target.substring(5));
        } else {
            String resolveUrl = authServiceUrl + "/api/credentials/telegram/chat-id/{userId}";
            try {
                HttpHeaders headers = getAuthHeaders();
                HttpEntity<Void> entity = new HttpEntity<>(headers);
                
                chatId = restTemplate.exchange(resolveUrl, HttpMethod.GET, entity, Long.class, Map.of("userId", target)).getBody();
                
                if (chatId == null) {
                    throw new IllegalArgumentException("User " + target + " is not linked to Telegram.");
                }
            } catch (Exception e) {
                if (e instanceof IllegalArgumentException) throw e;
                throw new RuntimeException("Failed to resolve telegram chat id: " + e.getMessage());
            }
        }

        String url = String.format("https://api.telegram.org/bot%s/sendMessage", botToken);
        try {
            restTemplate.getForObject(url + "?chat_id={chatId}&text={text}", String.class, 
                    Map.of("chatId", chatId, "text", message));
        } catch (Exception e) {
            throw new RuntimeException("Failed to send telegram message: " + e.getMessage());
        }
    }

    private HttpHeaders getAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null) {
                headers.set("Authorization", authHeader);
            }
        }
        return headers;
    }
}
