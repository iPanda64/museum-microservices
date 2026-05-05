package com.museum.auth.infrastructure.dao;

import com.museum.auth.domain.daocontracts.CredentialsDAO;
import com.museum.auth.domain.models.Credentials;
import com.museum.auth.domain.models.UserId;
import com.museum.auth.domain.models.Username;
import com.museum.auth.infrastructure.entities.CredentialsEntity;
import com.museum.auth.infrastructure.mappers.CredentialsMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Transactional
public class CredentialsDAOImpl implements CredentialsDAO {

    @PersistenceContext
    private EntityManager entityManager;

    private final CredentialsMapper credentialsMapper;

    public CredentialsDAOImpl(CredentialsMapper credentialsMapper) {
        this.credentialsMapper = credentialsMapper;
    }

    @Override
    public List<Credentials> findAll() {
        List<CredentialsEntity> entities = entityManager
                .createQuery("SELECT c FROM CredentialsEntity c", CredentialsEntity.class)
                .getResultList();

        return entities.stream()
                .map(credentialsMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Credentials> findById(UserId userId) {
        CredentialsEntity entity = entityManager.find(CredentialsEntity.class, userId.value());
        return Optional.ofNullable(credentialsMapper.toDomain(entity));
    }

    @Override
    public Optional<Credentials> findByUsername(Username username) {
        try {
            TypedQuery<CredentialsEntity> query = entityManager.createQuery(
                    "SELECT c FROM CredentialsEntity c WHERE c.username = :uname", CredentialsEntity.class);
            query.setParameter("uname", username.value());
            return Optional.of(credentialsMapper.toDomain(query.getSingleResult()));
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Credentials save(Credentials credentials) {
        CredentialsEntity entity = credentialsMapper.toEntity(credentials);

        if (entity.getUserId() == null) {
            entityManager.persist(entity);
        } else {
            entity = entityManager.merge(entity);
        }
        return credentialsMapper.toDomain(entity);
    }

    @Override
    public void deleteById(UserId userId) {
        CredentialsEntity entity = entityManager.find(CredentialsEntity.class, userId.value());
        if (entity != null) {
            entityManager.remove(entity);
        }
    }

    @Override
    public boolean existsById(UserId userId) {
        Long count = entityManager.createQuery(
                        "SELECT count(c) FROM CredentialsEntity c WHERE c.userId = :uid", Long.class)
                .setParameter("uid", userId.value())
                .getSingleResult();
        return count > 0;
    }
}
