package com.museum.auth.controllers;

import com.museum.auth.domain.dtos.LoginRequestDto;
import com.museum.auth.services.CredentialsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login")
public class LoginController {

    private final CredentialsService credentialsService;

    @PostMapping
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequest) {
        String token = credentialsService.login(loginRequest.username(), loginRequest.password());
        return ResponseEntity.ok(token);
    }
}
