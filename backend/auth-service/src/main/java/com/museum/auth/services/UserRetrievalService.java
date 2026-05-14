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
public class UserRetrievalService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final JwtTokenService jwtTokenService;

    @Value("${services.user.url}")
    private String userServiceUrl;

    public String getUserEmail(Integer userId) {
        String url = userServiceUrl + "/api/users/" + userId;
        
        HttpHeaders headers = new HttpHeaders();
        String token = jwtTokenService.getCurrentToken();
        if (token != null) {
            headers.set("Authorization", token);
        }

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        
        try {
            Map<String, Object> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class).getBody();
            if (response != null && response.containsKey("email")) {
                return (String) response.get("email");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch user email: " + e.getMessage());
        }
        
        throw new IllegalArgumentException("Could not find email for user " + userId);
    }
}
