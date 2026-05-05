package com.museum.auth.services;

import com.museum.auth.domain.daocontracts.CredentialsDAO;
import com.museum.auth.domain.models.Credentials;
import com.museum.auth.domain.models.UserId;
import com.museum.auth.domain.models.Username;
import com.museum.auth.domain.models.RoleName;
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
                credentials.roleId()
        );
        return credentialsDAO.save(credentialsToSave);
    }

    public void deleteCredentials(Integer id) {
        credentialsDAO.deleteById(new UserId(id));
    }
    public Credentials updateCredentials(Integer userId, Credentials updatedCredentials) {
        UserId id = new UserId(userId);
        boolean exists = credentialsDAO.existsById(id);
        if (!exists) {
            throw new IllegalArgumentException("Credentials with ID " + userId + " not found.");
        }

        String hashedPassword = passwordEncoder.encode(updatedCredentials.passwordHash());

        return credentialsDAO.save( new Credentials(
                id,
                updatedCredentials.username(),
                hashedPassword,
                updatedCredentials.roleId()
        ));
    }
    public String login(String username, String password) {
        Credentials credentials = credentialsDAO.findByUsername(new Username(username))
                .filter(c -> passwordEncoder.matches(password, c.passwordHash()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        String roleName = RoleName.fromId(credentials.roleId()).name();

        return jwtTokenService.generateToken(credentials.userId(), roleName);
    }
}
