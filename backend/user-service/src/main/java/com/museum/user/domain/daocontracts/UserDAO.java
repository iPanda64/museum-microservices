package com.museum.user.domain.daocontracts;

import com.museum.user.domain.aggregate.User;
import com.museum.user.domain.aggregate.UserId;

import java.util.List;
import java.util.Optional;

public interface UserDAO {

    List<User> findAll();

    Optional<User> findById(UserId userId);

    Optional<User> findByEmail(String email);

    User save(User user);

    void deleteById(UserId userId);

    boolean existsById(UserId userId);
}
