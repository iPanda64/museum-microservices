package com.museum.auth.services;

import com.museum.auth.domain.aggregate.RoleName;
import com.museum.auth.domain.aggregate.UserId;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service("jwtTokenService")
public class JwtTokenService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-ms}")
    private long jwtExpirationMs;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(UserId userId, String roleName) {
        return Jwts.builder()
                .subject(userId.value().toString())
                .claim("role", "ROLE_" + roleName)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    public String generateSystemAdminToken() {
        return "Bearer " + generateToken(new UserId(0), "ADMIN");
    }

    public String getCurrentToken() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                return authHeader;
            }
        }
        return null;
    }

    public RoleName extractRoleName(String token) {
        String roleStr = extractAllClaims(token).get("role", String.class);
        if (roleStr == null) return RoleName.USER;

        String cleanRole = roleStr.replace("ROLE_", "");
        return RoleName.valueOf(cleanRole);
    }

    public UserId extractUserId(String token) {
        String subject = extractAllClaims(token).getSubject();
        if (subject == null) return null;

        return new UserId(Integer.valueOf(subject));
    }

    public boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}
