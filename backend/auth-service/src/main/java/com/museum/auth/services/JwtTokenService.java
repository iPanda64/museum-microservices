package com.museum.auth.services;

import com.museum.auth.domain.aggregate.RoleName;
import com.museum.auth.domain.aggregate.UserId;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service("jwtTokenService")
public class JwtTokenService {

    private final SecretKey secureKey = Jwts.SIG.HS256.key().build();

    @Value("${jwt.expiration-ms}")
    private long jwtExpirationMs;

    private SecretKey getSigningKey() {
        return this.secureKey;
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
        return !isTokenExpired(token);
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
