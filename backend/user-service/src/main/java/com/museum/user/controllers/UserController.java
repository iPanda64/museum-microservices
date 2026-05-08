package com.museum.user.controllers;

import com.museum.user.controllers.mappers.UserControllerMapper;
import com.museum.user.domain.dtos.UserRequestDto;
import com.museum.user.domain.dtos.UserResponseDto;
import com.museum.user.domain.aggregate.User;
import com.museum.user.domain.aggregate.NullUser;
import com.museum.user.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserControllerMapper userControllerMapper;

    public UserController(UserService userService, UserControllerMapper userControllerMapper) {
        this.userService = userService;
        this.userControllerMapper = userControllerMapper;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(userControllerMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Integer userId) {
        return userService.getUserById(userId)
                .map(userControllerMapper::toResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> createUser(@PathVariable Integer userId, @RequestBody UserRequestDto dto) {
        User domain = userControllerMapper.toDomain(userId, dto);
        User created = userService.createUser(domain);
        return new ResponseEntity<>(userControllerMapper.toResponseDto(created), HttpStatus.CREATED);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Integer userId,
            @RequestBody UserRequestDto dto) {
        NullUser nullDomain = userControllerMapper.toNullDomain(dto);
        User updated = userService.updateUser(userId, nullDomain);
        return ResponseEntity.ok(userControllerMapper.toResponseDto(updated));
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
