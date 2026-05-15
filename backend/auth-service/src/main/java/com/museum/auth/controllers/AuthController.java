package com.museum.auth.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    @GetMapping("/verify-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> verifyAdmin() {
        return ResponseEntity.ok().build();
    }
    @GetMapping("/verify-manager")
    @PreAuthorize("hasRole('ADMIN')||hasRole('MANAGER')")
    public ResponseEntity<Void> verifyManager() {
        return ResponseEntity.ok().build();
    }
    @GetMapping("/verify-employee")
    @PreAuthorize("hasRole('ADMIN')||hasRole('MANAGER')||hasRole('EMPLOYEE')")
    public ResponseEntity<Void> verifyEmployee() {
        return ResponseEntity.ok().build();
    }
}
