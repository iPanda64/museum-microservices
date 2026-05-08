package com.museum.user.services;

import com.museum.user.domain.daocontracts.UserDAO;
import com.museum.user.domain.aggregate.User;
import com.museum.user.domain.aggregate.UserId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    public Optional<User> getUserById(Integer id) {
        return userDAO.findById(new UserId(id));
    }

    public User createUser(User user) {
        if (user.userId() != null && userDAO.existsById(user.userId())) {
            throw new IllegalArgumentException("User with this ID already exists!");
        }
        if (userDAO.findByEmail(user.email()).isPresent()) {
            throw new IllegalArgumentException("Email already exists!");
        }
        return userDAO.save(user);
    }

    public void deleteUser(Integer id) {
        userDAO.deleteById(new UserId(id));
    }

    public User updateUser(Integer userId, com.museum.user.domain.aggregate.NullUser nullUser) {
        User existing = userDAO.findById(new UserId(userId))
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found."));

        String finalEmail = existing.email();
        if (nullUser.email() != null && !nullUser.email().isBlank()) {
            finalEmail = nullUser.email();
            if (!finalEmail.equals(existing.email()) && userDAO.findByEmail(finalEmail).isPresent()) {
                throw new IllegalArgumentException("Email already exists!");
            }
        }

        String finalPhoneNumber = existing.phoneNumber();
        if (nullUser.phoneNumber() != null && !nullUser.phoneNumber().isBlank()) {
            finalPhoneNumber = nullUser.phoneNumber();
        }

        User userToSave = new User(existing.userId(), finalEmail, finalPhoneNumber);

        return userDAO.save(userToSave);
    }
}
