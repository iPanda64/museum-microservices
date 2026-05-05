package com.museum.auth.domain.daocontracts;

import com.museum.auth.domain.models.Credentials;
import com.museum.auth.domain.models.UserId;
import com.museum.auth.domain.models.Username;

import java.util.List;
import java.util.Optional;

public interface CredentialsDAO {

    List<Credentials> findAll();

    Optional<Credentials> findById(UserId userId);

    Optional<Credentials> findByUsername(Username username);

    Credentials save(Credentials credentials);

    void deleteById(UserId userId);

    boolean existsById(UserId userId);
}
