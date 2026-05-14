package com.museum.auth.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final JwtTokenService jwtTokenService;

    @Value("${services.notification.url}")
    private String baseUrl;

    public void sendTelegramNotification(String target, String message) {
        sendTelegramNotification(target, message, null);
    }

    public void sendTelegramNotification(String target, String message, String customToken) {
        String url = baseUrl + "/api/notification/telegram/" + target;
        HttpHeaders headers = new HttpHeaders();
        
        String token = customToken != null ? customToken : jwtTokenService.getCurrentToken();

        sendNotification(message, url, headers, token);
    }

    private void sendNotification(String message, String url, HttpHeaders headers, String token) {
        if (token != null) {
            headers.set("Authorization", token);
        }

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(Map.of("message", message), headers);
        restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);
    }

    public void sendEmailNotification(String email, String message) {
        String url = baseUrl + "/api/notification/email/" + email;
        HttpHeaders headers = new HttpHeaders();
        
        String token = jwtTokenService.getCurrentToken();
        sendNotification(message, url, headers, token);
    }
}
