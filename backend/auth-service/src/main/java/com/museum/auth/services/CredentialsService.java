package com.museum.auth.services;

import com.museum.auth.domain.daocontracts.CredentialsDAO;
import com.museum.auth.domain.aggregate.Credentials;
import com.museum.auth.domain.aggregate.UserId;
import com.museum.auth.domain.aggregate.Username;
import com.museum.auth.domain.aggregate.RoleName;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CredentialsService {

    private final CredentialsDAO credentialsDAO;
    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;
    private final UserRetrievalService userRetrievalService;

    public List<Credentials> getAllCredentials() {
        return credentialsDAO.findAll();
    }

    public Optional<Credentials> getCredentialsById(Integer id) {
        return credentialsDAO.findById(new UserId(id));
    }

    public Optional<Credentials> getCredentialsByUsername(String username) {
        return credentialsDAO.findByUsername(new Username(username));
    }

    public Credentials createCredentials(Credentials credentials) {
        boolean exists = credentialsDAO.findByUsername(credentials.username()).isPresent();
        if (exists) {
            throw new IllegalArgumentException("Username already exists!");
        }
        String hashedPassword = passwordEncoder.encode(credentials.passwordHash());
        Credentials credentialsToSave = new Credentials(
                credentials.userId(),
                credentials.username(),
                hashedPassword,
                credentials.roleId(),
                null
        );
        return credentialsDAO.save(credentialsToSave);
    }

    public void deleteCredentials(Integer id) {
        credentialsDAO.deleteById(new UserId(id));
    }

    public Credentials updateCredentials(Integer userId, com.museum.auth.domain.aggregate.NullCredentials nullCredentials) {
        Credentials existing = credentialsDAO.findById(new UserId(userId))
                .orElseThrow(() -> new IllegalArgumentException("Credentials with ID " + userId + " not found."));

        Username finalUsername = resolveUsername(existing, nullCredentials.username());
        
        boolean passwordChanged = isPasswordChanging(nullCredentials.password());
        String finalPasswordHash = passwordChanged 
                ? passwordEncoder.encode(nullCredentials.password()) 
                : existing.passwordHash();

        Integer finalRoleId = nullCredentials.roleId() != null 
                ? nullCredentials.roleId() 
                : existing.roleId();

        Credentials credentialsToSave = new Credentials(
                existing.userId(), 
                finalUsername, 
                finalPasswordHash, 
                finalRoleId, 
                existing.telegramChatId()
        );
        
        Credentials saved = credentialsDAO.save(credentialsToSave);

        if (passwordChanged) {
            sendPasswordChangeNotifications(userId, existing.telegramChatId());
        }

        return saved;
    }

    private Username resolveUsername(Credentials existing, Username newUsername) {
        if (newUsername == null) {
            return existing.username();
        }
        if (!newUsername.equals(existing.username()) && credentialsDAO.findByUsername(newUsername).isPresent()) {
            throw new IllegalArgumentException("Username already exists!");
        }
        return newUsername;
    }

    private boolean isPasswordChanging(String newPassword) {
        return newPassword != null && !newPassword.isBlank();
    }

    private void sendPasswordChangeNotifications(Integer userId, Long telegramChatId) {
        String email = userRetrievalService.getUserEmail(userId);
        notificationService.sendEmailNotification(email, "⚠️ Your password has been successfully changed.");
        
        if (telegramChatId != null) {
            notificationService.sendTelegramNotification("chat:" + telegramChatId, "⚠️ Your password has been successfully changed.");
        }
    }

    public String login(String username, String password) {
        Credentials credentials = credentialsDAO.findByUsername(new Username(username))
                .filter(c -> passwordEncoder.matches(password, c.passwordHash()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        String roleName = RoleName.fromId(credentials.roleId()).name();

        return jwtTokenService.generateToken(credentials.userId(), roleName);
    }
}
