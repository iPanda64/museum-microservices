package com.museum.user.infrastructure.dao;

import com.museum.user.domain.daocontracts.UserDAO;
import com.museum.user.domain.aggregate.User;
import com.museum.user.domain.aggregate.UserId;
import com.museum.user.infrastructure.entities.UserEntity;
import com.museum.user.infrastructure.mappers.UserMapper;
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
public class UserDAOImpl implements UserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    private final UserMapper userMapper;

    public UserDAOImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public List<User> findAll() {
        List<UserEntity> entities = entityManager
                .createQuery("SELECT u FROM UserEntity u", UserEntity.class)
                .getResultList();

        return entities.stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> findById(UserId userId) {
        UserEntity entity = entityManager.find(UserEntity.class, userId.value());
        return Optional.ofNullable(userMapper.toDomain(entity));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            TypedQuery<UserEntity> query = entityManager.createQuery(
                    "SELECT u FROM UserEntity u WHERE u.email = :email", UserEntity.class);
            query.setParameter("email", email);
            return Optional.of(userMapper.toDomain(query.getSingleResult()));
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public User save(User user) {
        UserEntity entity = userMapper.toEntity(user);

        if (entity.getUserId() == null || !existsById(new UserId(entity.getUserId()))) {
            entityManager.persist(entity);
        } else {
            entity = entityManager.merge(entity);
        }
        return userMapper.toDomain(entity);
    }

    @Override
    public void deleteById(UserId userId) {
        UserEntity entity = entityManager.find(UserEntity.class, userId.value());
        if (entity != null) {
            entityManager.remove(entity);
        }
    }

    @Override
    public boolean existsById(UserId userId) {
        if (userId == null || userId.value() == null) return false;
        Long count = entityManager.createQuery(
                        "SELECT count(u) FROM UserEntity u WHERE u.userId = :uid", Long.class)
                .setParameter("uid", userId.value())
                .getSingleResult();
        return count > 0;
    }
}
